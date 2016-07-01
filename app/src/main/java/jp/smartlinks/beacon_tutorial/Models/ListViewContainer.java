package jp.smartlinks.beacon_tutorial.Models;

/**
 * Created by hiro on 2016/02/12.
 */
public class ListViewContainer {

    public static final String TAG = ListViewContainer.class.getSimpleName();

    /**
     * TYPE定数値
     */
    public final static int TYPE_TITLE = 0;
    public final static int TYPE_BEACON = 1;
    public final static int TYPE_LOG = 2;

    private int type ;
    private int id;
    private Object object;

    /**
     * コンストラクタ
     * @param t タイプ（定数値)
     * @param o オブジェクト
     */
    public ListViewContainer( int t, int id, Object o ) {
        this.type = t;
        this.id = id;
        this.object = o;
    }

    public int GetType() {
        return this.type;
    }
    public Object GetObject() {
        return this.object;
    }
    public int GetId() { return this.id; }
}
