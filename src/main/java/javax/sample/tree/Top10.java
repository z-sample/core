package javax.sample.tree;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.Random;
import java.util.TreeSet;

/**
 * 从10亿个User中获取age最大的10用户(会按年龄去重)
 *
 * @author Zero
 *         Created on 2016/11/23.
 */
public class Top10 {
    public static void main(String[] args) throws Exception {
//        Process process = Runtime.getRuntime().exec("jcmd");
//        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//            buffer.lines().forEach(System.out::println);
//        }

        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        String jvmName = runtimeBean.getName();
        System.out.println("JVM Name = " + jvmName);
        long pid = Long.valueOf(jvmName.split("@")[0]);
        System.out.println("JVM PID  = " + pid);
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        int peakThreadCount = bean.getPeakThreadCount();
        System.out.println("Peak Thread Count = " + peakThreadCount);
        System.out.println("----------------------");

        long t0 = System.currentTimeMillis();

        /////////////////////////////////////////////////////////////////////////////

        TreeSet<User> users = new TreeSet<User>(); //User必须实现java.lang.Comparable
        //Top 10
        int top = 10;
        Random random = new Random();
        //10亿
        for (long i = 0; i < 1000 * 1000 * 1000; i++) {
            //预先创建10亿User会导致OutOfMemoryError
            User user = new User("zero", Math.abs(random.nextInt(200)));//zero:12s, zero + i:52s ,StringBuilder:50s => 拼接字符串效率低

            if (users.size() < top) {
                users.add(user);
            } else {
                User last = users.last();
                //需要先判断users.contains(user)
                if (last.age < user.age&&!users.contains(user)) {
                    users.pollLast();
                    users.add(user);
                }
            }
        }

        /////////////////////////////////////////////////////////////////////////////


        long t1 = System.currentTimeMillis();
        System.out.println((t1 - t0) / 1000 + "s");//测试时: 12s ,实际上会快很多(需要去除创建User对象和随机数的时间),
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        System.out.println("totalMemory: " + totalMemory / (1024 * 1024));//195
        System.out.println("freeMemory: " + freeMemory / (1024 * 1024));//99
        System.out.println("maxMemory: " + maxMemory / (1024 * 1024));//1781

        System.out.println(users);
    }


    public static class User implements Comparable<User> {
        String name;
        int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }


        //HashSet 是否重复是由hashCode()和equals()决定的
        //TreeSet 是否重复是由compareTo()返回值判断的,为0则表示重复
        @Override
        public int compareTo(User o) {
            return Integer.compare(o.age, this.age);//倒序
        }


        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("User{");
            sb.append("name='").append(name).append('\'');
            sb.append(", age=").append(age);
            sb.append('}');
            return sb.toString();
        }
    }


}
