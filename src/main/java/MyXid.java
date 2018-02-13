import javax.transaction.xa.Xid;

public class MyXid implements Xid {
    //blabla i want to make a conflict too
    //blabla i want to make a conflict
    //finally team lead resolved it!
    protected int formatId;
    protected byte gtrid[];
    protected byte bqual[];

    public MyXid() {
    }

    public MyXid(int formatId, byte gtrid[], byte bqual[]) {
        this.formatId = formatId;
        this.gtrid = gtrid;
        this.bqual = bqual;
    }

    public int getFormatId() {
        return formatId;
    }

    public byte[] getBranchQualifier() {
        return bqual;
    }

    public byte[] getGlobalTransactionId() {
        return gtrid;
    }
}
