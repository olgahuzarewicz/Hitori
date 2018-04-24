import java.awt.Point;

public class HitoriPuzzle {
	//Point p;
	int x;
	int y;
	int value;
	boolean shaded;
	boolean checked;
	boolean visited;
	
	HitoriPuzzle(int x, int y, int v, boolean s){
		//p = new Point(x, y);
		this.x = x;
		this.y = y;
		this.value=v;
		this.shaded=s;
		this.checked=false;
		this.visited=false;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isShaded() {
		return shaded;
	}

	public void setShaded(boolean shaded) {
		this.shaded = shaded;
	}

}
