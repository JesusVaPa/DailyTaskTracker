package com.javap.dailytasktracker.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ClientsViewModel extends ViewModel {
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> user;
    private final MutableLiveData<Boolean> loginFailed;

    public ClientsViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        user = new MutableLiveData<>();
        loginFailed = new MutableLiveData<>();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            user.setValue(currentUser);
        }
    }

    public void loginUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            loginFailed.setValue(true);
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.setValue(firebaseAuth.getCurrentUser());
                        loginFailed.setValue(false);
                    } else {
                        user.setValue(null);
                        loginFailed.setValue(true);
                    }
                });
    }

    public void signUpUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            loginFailed.setValue(true);
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.setValue(firebaseAuth.getCurrentUser());
                        loginFailed.setValue(false);
                    } else {
                        user.setValue(null);
                        loginFailed.setValue(true);
                    }
                });
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public LiveData<Boolean> getLoginFailed() {
        return loginFailed;
    }
}
