package com.iyuriy;

public class App {
    class Tuple<K, V> {
        K key = null;
        V value = null;

        public Tuple(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public String toString() {
            return key + " -> " + value;
        }
    }

    public static void main(String[] args) {
//        try {
//            Runtime.getRuntime().exec("jstat " + pid + ">" + out.toString());
//        } catch (IOException e){
//            e.printStackTrace();
//        }
        try {
            Class cl = Class.forName("com.iyuriy.MyClassLoader");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        App app = new App();
//        app.run();
    }
//
//    private void initClasses() {
//        try {
//            Class cl = ClassLoader.getSystemClassLoader().loadClass("java.lang.String");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void run() {
//
//        initClasses();
//        printLoadedClasses();
//    }
//
//    private void printLoadedClasses() {
//        Instrumentation inst = InstrumentationCL.getInstrumentation();
//        List<Tuple<String, String>> list = new ArrayList<>();
//        for (Class<?> cl : inst.getAllLoadedClasses()) {
//            ClassLoader clazz = cl.getClassLoader();
//            if (clazz != null) {
//                list.add(new Tuple<>(cl.getClassLoader().getName(),
//                        cl.getCanonicalName()));
//            } else {
//                list.add(new Tuple<>("empty",
//                        cl.getCanonicalName()));
//            }
//        }
//
//        list.stream().filter(tuple -> tuple.getValue() != null)
////                .sorted(Comparator.comparing(Tuple::getValue))
//                .forEach(System.out::println);
//    }
}
