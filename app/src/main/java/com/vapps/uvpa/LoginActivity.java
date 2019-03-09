package com.vapps.uvpa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import java.util.List;



public class LoginActivity extends AppCompatActivity
{

    EditText usernameEditText;

    EditText passwordEditText;

    CoordinatorLayout coordinatorLayout;



    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private CallbackManager mCallbackManager;



      /*
          // if (view.getId() == R.id.changeSignupModeTextView) {

       //         Button signupButton = (Button) findViewById(R.id.signupButton);

                if (signUpModeActive) {

                    signUpModeActive = false;
                  //  signupButton.setText("Login");
                    changeSignupModeTextView.setText("Or, Signup");

                } else {

                    signUpModeActive = true;
                   // signupButton.setText("Signup");
                    changeSignupModeTextView.setText("Or, Login");

                }

            }



        public void signUp(View view) {

            EditText usernameEditText = (EditText) findViewById(R.id.editText);

            EditText passwordEditText = (EditText) findViewById(R.id.editText2);

            if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {

                Toast.makeText(this, "A username and password are required.", Toast.LENGTH_SHORT).show();

            } else {

                if (signUpModeActive) {

                    ParseUser user = new ParseUser();

                    user.setUsername(usernameEditText.getText().toString());
                    user.setPassword(passwordEditText.getText().toString());

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                Log.i("Signup", "Successful");

                            } else {

                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                } else {

                    ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {

                            if (user != null) {

                                Log.i("Signup", "Login successful");

                            } else {

                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }


                        }
                    });


                }
            }


        }
*/

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
          //  changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);

        //    changeSignupModeTextView.setOnClickListener(this);

        /*    retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.12:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            APIInterface apiInterface =  retrofit.create(APIInterface.class);
            apiInterface.getBookName().enqueue(new Callback<Books>() {
                @Override
                public void onResponse(Call<Books> call, Response<Books> response) {
                   Books books = response.body();
                    //ip.setText(ipAddress.ip);

                    Toast.makeText(LoginActivity.this,books.name,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Books> call, Throwable t) {
                    Toast.makeText(LoginActivity.this,"Connection error",Toast.LENGTH_SHORT).show();
                }
            });
*/



            usernameEditText = (EditText) findViewById(R.id.username);

             passwordEditText = (EditText) findViewById(R.id.password);




           // drawer = findViewById(R.id.drawer_layout);

            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(getString(R.string.server_client_id))
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mAuth = FirebaseAuth.getInstance();

           // mCallbackManager = CallbackManager.Factory.create();




/*            Parse.enableLocalDatastore(this);





            Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                    .applicationId("484408b35caef616b69b3586abeadce608c74b7f")
                    .clientKey("a5662c611f3cc32b674a8396051151ee649fcbd7")
                    .server("http://18.188.126.143:80/parse/")
                    .build()


            );



            ParseACL defaultACL = new ParseACL();
            defaultACL.setPublicReadAccess(true);
            defaultACL.setPublicWriteAccess(true);
            ParseACL.setDefaultACL(defaultACL, true);
            ParseAnalytics.trackAppOpenedInBackground(getIntent());
            */








        }


        public void SignInIcon(View view)

        {
            String email = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                startActivity(new Intent(LoginActivity.this,MapsActivity.class));

                            } else
                                {

                                Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();

                            }
                        }




                    });



        }

    public void signUpButton(View view)
    {
        startActivity(new Intent(LoginActivity.this,SignUp.class));

    }

    public void GsignIn(View view)
    {

if (FirebaseAuth.getInstance().getCurrentUser() == null)
{
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
    Toast.makeText(this,"not null",Toast.LENGTH_LONG).show();
  //  startActivity(new Intent(LoginActivity.this,MapsActivity.class));
}

else
{
    startActivity(new Intent(LoginActivity.this,MapsActivity.class));
}


    }

   /* public void drawerClick(View view)
    {
        startActivity(new Intent(LoginActivity.this,QrGen.class));
    }*/




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       // mCallbackManager.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Toast.makeText(this," rcode fine ",Toast.LENGTH_LONG).show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase

                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this,account.toString(),Toast.LENGTH_LONG).show();

                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
               // Snackbar.make(coordinatorLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                // ...
            }
        }
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this," rcodeq fine ",Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this,MapsActivity.class));

                        } else {
                            Toast.makeText(LoginActivity.this," error ",Toast.LENGTH_LONG).show();
                            // If sign in fails, display a message to the user.

                         //   Snackbar.make(coordinatorLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }
                    }


                });
    }



    public void FbSignIn(View view)
    {
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

                // ...
            }

            @Override
            public void onError(FacebookException error) {

                // ...
            }
        });



    }

    private void handleFacebookAccessToken(AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();



                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


}



