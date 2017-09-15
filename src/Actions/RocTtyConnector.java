package Actions;

import com.jediterm.terminal.ProcessTtyConnector;
import com.pty4j.PtyProcess;
import com.pty4j.WinSize;

import java.nio.charset.Charset;

class RocTtyConnector extends ProcessTtyConnector
{
    private PtyProcess myProcess;

    RocTtyConnector(PtyProcess process, Charset charset)
    {
        super(process, charset);
        this.myProcess = process;
    }

    @Override
    public String getName()
    {
        return "Roc JS";
    }

    protected void resizeImmediately() {
        if (this.getPendingTermSize() != null && this.getPendingPixelSize() != null) {
            this.myProcess.setWinSize(new WinSize(this.getPendingTermSize().width, this.getPendingTermSize().height, this.getPendingPixelSize().width, this.getPendingPixelSize().height));
        }
    }

    public boolean isConnected() {
        return this.myProcess.isRunning();
    }
}
