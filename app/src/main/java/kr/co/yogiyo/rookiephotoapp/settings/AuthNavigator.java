package kr.co.yogiyo.rookiephotoapp.settings;

public interface AuthNavigator {
    void createUserWithEmailAndPassword(final String email, final String password, final LoginCallback callback);

    void signInWithEmailAndPassword(final String email, final String password, final LoginCallback callback);
}
