package services;

public class Config {

    // firebase global address
   // public static final String GLOBAL_ADDRESS = "https://ismartnaija-7a16b.firebaseio.com/";
    public static final String GLOBAL_ADDRESS = "https://healthfirst-76127.firebaseio.com/";
    public static final String FIRE_BASE_USER_URL = GLOBAL_ADDRESS+"users";
    public static final String FIRE_BASE_DOCTOR_URL = GLOBAL_ADDRESS+"doctor_users";

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}
