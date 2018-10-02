package vadimjprokopev.imageDecolorizer;

public class ColorPoint {
    int red;
    int green;
    int blue;

    public ColorPoint(int RGB) {
        int mask = 0b11111111;
        blue = mask & RGB;
        green = mask & (RGB >>> 8);
        red = mask & (RGB >>> 16);
    }

    public ColorPoint(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public double vectorDistance(ColorPoint another) {
        return Math.sqrt(Math.pow(this.red - another.red, 2)
        		+ Math.pow(this.green - another.green, 2)
        		+ Math.pow(this.blue - another.blue, 2));
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + blue;
        result = prime * result + green;
        result = prime * result + red;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ColorPoint other = (ColorPoint) obj;
        if (blue != other.blue)
            return false;
        if (green != other.green)
            return false;
        if (red != other.red)
            return false;
        return true;
    }
}
