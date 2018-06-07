package services;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.ignite.cache.CacheAtomicityMode.TRANSACTIONAL;

public abstract class AbctractServiceOperation implements Service, ServiceOperation {
    @IgniteInstanceResource
    private Ignite ignite;

    static volatile private IgniteCache<String, Map<String, AtomicInteger> >  statistic;

    private String name;

    @Override public void init(ServiceContext ctx) throws Exception {
        name = ctx.name();


        System.out.println(name);
        Map <String, AtomicInteger> map = new ConcurrentHashMap<>();
        map.put("countExecute", new AtomicInteger(0) );
        map.put("countSvs",  new AtomicInteger(0));
        statistic = ignite.getOrCreateCache(new CacheConfiguration()
                .setName("statistics")
                .setAtomicityMode(TRANSACTIONAL));
        statistic.put(name, map);

        ignite.log().info(
                "Service instance " + this
                        + " initialized on node "
                        + ignite.cluster().localNode());
    }

    @Override public void execute(ServiceContext ctx) throws Exception {
        ignite.log().info(
                "Service instance " + this
                        + " executed on node "
                        + ignite.cluster().localNode());
    }

    @Override public void cancel(ServiceContext ctx) {
        ignite.log().info(
                "Service instance " + this
                        + " canceled on node "
                        + ignite.cluster().localNode());
    }


    protected void getStatistic(){
        Map<String, AtomicInteger> stat =  statistic.get(name);
        AtomicInteger num = stat.get("countExecute");
        num.incrementAndGet();

        stat.put("countExecute", num);
        statistic.put(name, stat);
    }

}
