public class Rectangle{
	private double width = 1;
	private double height = 1;

	Rectangle(){}
	Rectangle(double width,double height){
		this.width = width;
		this.height = height;
	}

	double getArea(){
		return width*height;
	}

	double getPerimeter(){
		return (width+height)*2;
	}
	public static void main(String[] args) {
		Rectangle r1 = new Rectangle(4, 10);
		Rectangle r2 = new Rectangle(3.5, 35.9);
		System.out.println(r1.width+" "+r1.height+" "+r1.getArea()+" "+r1.getPerimeter());
		System.out.println(r2.width+" "+r2.height+" "+r2.getArea()+" "+r2.getPerimeter());
	}
}