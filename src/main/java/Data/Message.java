package Data;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("content")
    private String content;
    @SerializedName("destinataire")
    private String destinataire;
    @SerializedName("expediteur")
    private String expediteur;


    public Message(String content, String destinataire, String expediteur) {
        this.content = content;
        this.destinataire = destinataire;
        this.expediteur = expediteur;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(String expediteur) {
        this.expediteur = expediteur;
    }
}

