package netty.thing;

import java.io.Serializable;

public class Message implements Serializable {
    String id;
    Object res;
    int type;
    //0 -> req ,1 -> resp

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getRes() {
        return res;
    }

    public void setRes(Object res) {
        this.res = res;
    }

    public Message(String id,int type, Object res) {
        this.id = id;
        this.res = res;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", res=" + res +
                '}';
    }
}
