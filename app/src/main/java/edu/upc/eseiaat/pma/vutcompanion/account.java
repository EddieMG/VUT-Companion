package edu.upc.eseiaat.pma.vutcompanion;

//Creem una classe per a l'objecte "account", el qual emmagatzemarà el seu nom (email) i un boolea
//per a saber si està seleccionat

public class account {
    private String text;
    private boolean checked;

    public account(String text) {
        this.text = text;
        this.checked = false;
    }

    public account(String text, boolean checked) {
        this.text = text;
        this.checked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void toggleChecked() {
        this.checked = !this.checked;
    }
}