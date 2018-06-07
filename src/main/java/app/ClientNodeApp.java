package app;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgnitePredicate;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import services.*;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import services.operation.*;
import utils.OperationHolder;
import utils.ServiceFilter;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class ClientNodeApp {
    public static void main(String[] args)  throws InterruptedException{

        Ignition.setClientMode(true);
        Ignite ignite = Ignition.start(
                new IgniteConfiguration()
                        .setUserAttributes(
                                Collections.singletonMap("svc-node", "true")
                        )
                        .setDiscoverySpi(new TcpDiscoverySpi()
                                .setIpFinder(new TcpDiscoveryVmIpFinder()
                                        .setAddresses(Collections.singletonList("127.0.0.1"))))
                        .setServiceConfiguration(
                               )
        );

        ClusterGroup cacheGrp = ignite.cluster().forCacheNodes("operations");

        deployNodeService( "add",  new OperationAdd(),  cacheGrp,  ignite);
        deployNodeService( "sub",  new OperationSub(),  cacheGrp,  ignite);
        deployNodeService( "mult", new OperationMult(), cacheGrp,  ignite);
        deployNodeService( "div",  new OperationDiv(),  cacheGrp,  ignite);

        ServiceOperation addSvc = ignite.services().
                serviceProxy("add", ServiceOperation.class, false);
        ServiceOperation subSvc = ignite.services().
                serviceProxy("sub", ServiceOperation.class, false);
        ServiceOperation multSvc = ignite.services().
                serviceProxy("mult", ServiceOperation.class, false);
        ServiceOperation divSvc = ignite.services().
                serviceProxy("div", ServiceOperation.class, false);



        OperationHolder holder = new OperationHolder(addSvc, subSvc, multSvc, divSvc);


        System.out.println("----------------------------------------------------");
        System.out.println("Result:");
        System.out.println(holder.calc("54+54+435-6-68*45*34/56/234/23*343"));
        System.out.println("----------------------------------------------------");
        IgniteCache<String, Map> cache = ignite.cache("statistics");
        Map <String, AtomicInteger> stat = cache.get("add");
        System.out.println(stat.get("countExecute"));
        System.out.println(stat.get("countSvs"));

        stat = cache.get("sub");
        System.out.println(stat.get("countExecute"));

        stat = cache.get("mult");
        System.out.println(stat.get("countExecute"));

        stat = cache.get("div");
        System.out.println(stat.get("countExecute"));

        if( stat.get("countExecute").get() > 10){
            deployNodeService( "div",  new OperationDiv(),  cacheGrp,  ignite);
        }


    }

    static private  void getProxy(Ignite ignite){

    }


    static private void deployNodeService(String name, Service service, ClusterGroup cacheGrp, Ignite ignite){
        ServiceConfiguration cfg = new ServiceConfiguration()
                .setName(name)
                .setService(service)
               .setMaxPerNodeCount(1)
               .setTotalCount(1)
                .setNodeFilter(new ServiceFilter(name));

        ignite.services(cacheGrp).deploy(cfg);

    }
}


