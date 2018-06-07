package utils;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgnitePredicate;
import org.apache.ignite.resources.IgniteInstanceResource;

import javax.sound.midi.Soundbank;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceFilter implements IgnitePredicate<ClusterNode> {

        @IgniteInstanceResource
        private Ignite ignite;

        private String name;

        public ServiceFilter(String name) {
            this.name = name;
        }

        @Override
        public boolean apply(ClusterNode clusterNode) {
            Boolean dataNode = clusterNode.attribute("operation");
            return !clusterNode.isClient() && dataNode &&deploy();
        }

        private boolean deploy() {
            System.out.println("sdf");
            IgniteCache<String, Map> cache = ignite.cache("statistics");
            try {

                Map<String, AtomicInteger> stat = cache.get(name);

                AtomicInteger countSvs =  stat.get("countExecute");
                AtomicInteger countExecute =  stat.get("countExecute");
                System.out.println(countSvs);
                System.out.println(countExecute);


                if (countSvs.getAndIncrement() == 0) {


                    return true;
                }
                if (countExecute.get()/countSvs.getAndIncrement() > 10){
                    return true;
                }
                return false;

            }catch (NullPointerException e){

                return true;

            }

        }

}
