package Data;

public enum RequestCode {

    ACCOUNT(1),
    CHAT_CONNECT(2),
    DECONNEXION(3),
    SEND_MESSAGE(4),
    ADD(5),
    CREATE_GROUP(6),
    CHANGE_PASSWORD(7),
    CHANGE_LOGIN(8),
    HISTORY(9);

    private int numero;

    RequestCode(int numero) {
        this.numero = numero;
    }

    public String toString() {
        return Integer.toString(this.numero);
    }

}
