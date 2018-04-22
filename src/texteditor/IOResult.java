package texteditor;

public class IOResult<T> {
	public static final boolean IO_SUCCESS = true;
	public static final boolean IO_FAILURE = false;
	
	private T data;
	private boolean ok;
	private Exception err;
	
	public IOResult(boolean ok, T data, Exception err) {
		this.ok = ok;
		this.data = data;
		this.err = err;
	}
	
	public boolean isOk() {
		return ok;
	}
	
	public boolean hasData() {
		return data != null;
	}
	
	public T getData() {
		return data;
	}
	
	public Exception getError() {
		return err;
	}

}
