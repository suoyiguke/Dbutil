package crawer.cyy.cnooc;

/**
 * @author xh
 * @date 18-04-04 004
 */
public class CnoocTiming {

    private int start=1;
    private int end=1;

    private int threads = 20;
    private long executeInterval = 1000;


    public void execute(){
        CnoocCrawerService cnoocCrawerService = new CnoocCrawerService(start,end);
        cnoocCrawerService.setThreads(threads);
        cnoocCrawerService.setExecuteInterval(executeInterval);
        cnoocCrawerService.execute();
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public void setExecuteInterval(long executeInterval) {
        this.executeInterval = executeInterval;
    }
}
