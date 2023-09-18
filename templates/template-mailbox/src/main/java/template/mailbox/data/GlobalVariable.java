package template.mailbox.data;

import java.util.ArrayList;
import java.util.List;

import template.mailbox.model.Mail;
import template.mailbox.model.People;

public class GlobalVariable {

    private static List<People> peoples = new ArrayList<>();
    private static List<Mail> inbox = new ArrayList<>();
    private static List<Mail> outbox = new ArrayList<>();
    private static List<Mail> trash = new ArrayList<>();
    private static List<Mail> spam = new ArrayList<>();

    public static List<People> getPeoples() {
        return peoples;
    }

    public static void setPeoples(List<People> peoples) {
        GlobalVariable.peoples = peoples;
    }

    public static List<Mail> getInbox() {
        return GlobalVariable.inbox;
    }

    public static void setInbox(List<Mail> inbox) {
        GlobalVariable.inbox = inbox;
    }

    public static void trashInbox(int position) {
        GlobalVariable.trash.add(inbox.get(position));
        GlobalVariable.inbox.remove(position);
    }

    public static void addInbox(Mail mail) {
        GlobalVariable.inbox.add(0, mail);
    }

    public static void undoTrashInbox(Mail m, int position) {
        GlobalVariable.inbox.add(position, m);
        GlobalVariable.removeTrash(m);
    }

    public static List<Mail> getOutbox() {
        return GlobalVariable.outbox;
    }

    public static void addOutbox(Mail mail) {
        GlobalVariable.outbox.add(0, mail);
    }

    public static void setOutbox(List<Mail> outbox) {
        GlobalVariable.outbox = outbox;
    }

    public static void trashOutbox(int position) {
        GlobalVariable.trash.add(outbox.get(position));
        GlobalVariable.outbox.remove(position);
    }

    public static void undoTrashOutbox(Mail m, int position) {
        GlobalVariable.outbox.add(position, m);
        GlobalVariable.removeTrash(m);
    }

    public static List<Mail> getTrash() {
        return GlobalVariable.trash;
    }

    public static void setTrash(List<Mail> trash) {
        GlobalVariable.trash = trash;
    }

    public static void addTrash(Mail m) {
        GlobalVariable.trash.add(m);
    }

    private static void removeTrash(Mail mail) {
        if (GlobalVariable.trash.contains(mail)) {
            GlobalVariable.trash.remove(mail);
        }
    }
}
