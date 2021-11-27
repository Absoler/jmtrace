public class OutputInfo {
    static String build_info(boolean w, Object obj, String name) {
        String res = (w ? "W" : "R");
        res += ' ';
        res += (Thread.currentThread()).getId();
        res += ' ';
        res += Integer.toHexString(System.identityHashCode(obj));
        res += ' ';
        res += name;
        return res;
    }

    static void trace_writeField(Object object, String name) {
        System.out.println(build_info(true, object, name));
    }

    static void trace_readField(Object object, String name) {
        System.out.println(build_info(false, object, name));
    }

    static void trace_writeStatic(Object object, String name) {
        System.out.println(build_info(true, object, name));
    }

    static void trace_readStatic(Object object, String name) {
        System.out.println(build_info(false, object, name));
    }

    static void trace_writeArray(Object array,int index){
        String arrayType = array.getClass().getCanonicalName();
        System.out.println(build_info(true, array, arrayType.substring(0, arrayType.length() - 2) + '[' + index + ']'));
    }

    static void trace_readArray(Object array, int index) {
        String arrayType = array.getClass().getCanonicalName();
        System.out.println(build_info(false, array, arrayType.substring(0, arrayType.length() - 2) + '[' + index + ']'));
    }
}
