package icu.twtool.ktor.cloud.discovery.polaris

import com.tencent.polaris.api.core.ConsumerAPI
import com.tencent.polaris.api.pojo.SourceService
import com.tencent.polaris.api.rpc.GetOneInstanceRequest
import com.tencent.polaris.api.rpc.InstanceRegisterRequest
import com.tencent.polaris.factory.ConfigAPIFactory
import com.tencent.polaris.factory.api.DiscoveryAPIFactory
import com.tencent.polaris.factory.config.ConfigurationImpl
import icu.twtool.ktor.cloud.HostKey
import icu.twtool.ktor.cloud.KtorCloudApplication
import icu.twtool.ktor.cloud.PortKey
import icu.twtool.ktor.cloud.config.core.KtorCloudConfiguration
import icu.twtool.ktor.cloud.discovery.core.Registry
import icu.twtool.ktor.cloud.discovery.core.ServiceInstance
import icu.twtool.ktor.cloud.discovery.core.ServiceName
import icu.twtool.ktor.cloud.discovery.core.ServiceProtocolKey
import org.slf4j.LoggerFactory
import java.net.InetAddress

class PolarisRegistry : Registry() {

    private val log = LoggerFactory.getLogger(PolarisRegistry::class.java)

    private var consumer: ConsumerAPI? = null
    private lateinit var config: KtorCloudConfiguration
    private lateinit var instance: ServiceInstance

    override fun KtorCloudApplication.register() {
        this@PolarisRegistry.config = config

        val polarisConfig = (ConfigAPIFactory.defaultConfig() as ConfigurationImpl).apply {
            global.serverConnector.addresses = config[AddressesKey]
        }
        consumer = DiscoveryAPIFactory.createConsumerAPIByConfig(polarisConfig)

        val host = config[HostKey].run {
            if (this == "0.0.0.0") InetAddress.getLocalHost().hostAddress
            else this
        }
        instance = ServiceInstance(config[ServiceName], config[ServiceProtocolKey], host, config[PortKey])

        val provider = DiscoveryAPIFactory.createProviderAPIByConfig(polarisConfig)
        provider.registerInstance(InstanceRegisterRequest().apply {
            service = instance.name
            namespace = config[NamespaceKey]
            protocol = instance.protocol

            this.host = instance.host
            port = instance.port
        })
    }

    override fun getInstance(serviceName: String): ServiceInstance? {
        val resp = consumer?.getOneInstance(GetOneInstanceRequest().let {
            it.namespace = config[NamespaceKey]     // 设置服务命名空间
            it.service = serviceName                // 设置服务名称
            it.timeoutMs = 100                      // 设置超时时间

            // 如果需要走 Hash 负载均衡的话，需要设置
            //it.criteria = Criteria().let { criteria ->
            //    criteria
            //}

            // ==== 可选 ====
            //it.metadata = mapOf<String, String>()                                 // 元数据信息，仅用于dstMetadata路由插件的过滤
            //it.metadataFailoverType = MetadataFailoverType.METADATAFAILOVERNONE   // 设置元数据路由兜底措施
            //it.method = method?.value                                             // 对应自定义路由规则中请求标签中的方法(Method)
            it.setServiceInfo(SourceService().let { source ->                       // 设置主调服务信息，只用于路由规则匹配
                source.namespace = config[NamespaceKey]     // 设置主调服务命名空间
                source.service = instance.name               // 设置主调服务名称
                //source.arguments = setOf<RouteArgument>()                         // 设置主调方的请求标签信息
                source
            })

            it
        })

        return resp?.instance?.takeIf { it.isHealthy }?.let {
            ServiceInstance(
                name = serviceName,
                protocol = it.protocol,
                host = it.host,
                port = it.port
            )
        }
    }
}