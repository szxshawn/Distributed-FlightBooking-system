package utils;

public class Arguments {
        //define the rpc is idempotent or not
        public static final boolean IDEMPOTENT = true;
        public static final boolean NON_IDEMPOTENT = false;



        //define the data transmission and execution status
        public static final int OK = 200;
        public static final int BAD_REQUEST = 400;
        public static final int REJECT = 423;
        public static final int INTERNAL_SERVER_ERROR = 500;



        //define the remote procedure call id
        public static final int TERMINATE_SERVER = 0;
        public static final int QUERY_FLIGHT_BY_SRC_AND_DEST = 1;
        public static final int QUERY_FLIGHT_BY_ID = 2;
        public static final int RESERVE_SEAT_BY_ID = 3;
        public static final int MONITOR_FLIGHT_BY_USER = 4;
        public static final int RPC_5 = 5;
        public static final int RPC_6 = 6;

}
