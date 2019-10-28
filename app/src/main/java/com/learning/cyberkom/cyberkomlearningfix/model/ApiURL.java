package com.learning.cyberkom.cyberkomlearningfix.model;

public class ApiURL {
    public static final String URL = "https://seger.pmiikarawang.or.id/";
//    public static final String URL = "http://192.168.100.4/santri_konsultan/";
    public static final String SignIn = URL + "index.php";
    public static final String SignUp = URL + "register.php";
    public static final String Upload = URL + "upload.php";
    public static final String ViewMateri = URL + "ViewLearn1.php";
    public static final String saveVideo = URL + "saveMateri.php";
    public static final String saveVideo2 = URL + "saveMateri2.php";
    public static final String saveNilai = URL + "saveMateri3.php";
    public static final String editMateri = URL + "editMateri.php";
    public static final String deleteMateri = URL + "deleteMateri.php";
    public static final String viewAccount = URL + "ViewAccount.php";
    public static final String deleteUsers = URL + "deleteAccount.php";
    public static final String viewid = URL + "ViewID.php";
    public static final String editphoto = URL + "uploadPhoto.php";
    public static final String detailaccount = URL + "DetailAccount.php";
    public static final String edit_account = URL + "editAccount.php";

    public static String getEdit_account() {
        return edit_account;
    }

    public static String getDetailaccount() {
        return detailaccount;
    }

    public static String getEditphoto() {
        return editphoto;
    }

    public static String getSignIn() {
        return SignIn;
    }

    public static String getDeleteUsers() {
        return deleteUsers;
    }

    public static String getViewAccount() {
        return viewAccount;
    }

    public static String getDeleteMateri() {
        return deleteMateri;
    }

    public static String getSaveNilai() {
        return saveNilai;
    }

    public static String getSignUp() {
        return SignUp;
    }

    public static String getUpload() {
        return Upload;
    }

    public static String getViewMateri() {
        return ViewMateri;
    }

    public static String getSaveVideo() {
        return saveVideo;
    }

    public static String getSaveVideo2() {
        return saveVideo2;
    }

    public static String getEditMateri() {
        return editMateri;
    }

    public static String getViewid() {
        return viewid;
    }
}
