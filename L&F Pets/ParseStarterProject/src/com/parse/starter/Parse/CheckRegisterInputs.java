package com.parse.starter.Parse;

/**
 * Created by Helena on 4/10/2015.
 */
public class CheckRegisterInputs {
    String errorMessage;

    public String checkInputs(String name,  String tel, String email, String pass){
        if (name != null && !name.isEmpty()  && tel != null && !tel.isEmpty() && pass != null && !pass.isEmpty()) {
            if (isValidEmail(email) ) {
                if(pass.length()>=6) {

                        if (checkPass(pass, name, email)) {
                            return "true";
                        }else{
                            return errorMessage;
                        }

                }else{
                    return errorMessage = "Molimo unesite lozinku od barem 6 znakova!";
                }
            }else{
                return errorMessage = "Molimo unesite ispravni email!";
            }
        }else{
            return errorMessage = "Molimo ispunite sva polja!";
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public  boolean checkPass (CharSequence target, CharSequence name, CharSequence email){
        if (target.equals(name)){
            errorMessage = "Lozinka ne može biti ista kao ime!";
            return false;

        }else if (target.equals(email)){
            errorMessage = "Lozinka ne može biti ista kao email!";
            return false;
        }else {
            return true;
        }
    }
}
