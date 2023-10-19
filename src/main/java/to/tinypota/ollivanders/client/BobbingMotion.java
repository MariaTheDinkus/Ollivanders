package to.tinypota.ollivanders.client;

public class BobbingMotion {

    private double frequency;  // Controls how fast the bobbing happens
    private double amplitude;  // Controls the height of the bobbing motion
    private double resetThreshold;  // The number at which the motion should reset

    public BobbingMotion(double frequency, double amplitude, double resetThreshold) {
        this.frequency = frequency * 2 * Math.PI / resetThreshold;
        this.amplitude = amplitude;
        this.resetThreshold = resetThreshold;
    }

    /**
     * Get the vertical position based on a sine wave that resets after a threshold.
     * @param t Time parameter which increases.
     * @return Value representing the up and down position.
     */
    public double getPosition(double t) {
        // Reset t after it reaches the threshold
//        t %= resetThreshold;
        
        // Calculate the sine wave value for bobbing motion
        return amplitude * Math.sin(frequency * t);
    }

    public static void main(String[] args) {
        BobbingMotion bobbing = new BobbingMotion(0.1, 1, 100);

        for (double i = 0; i <= 200; i += 1) {
            System.out.println("t: " + i + ", position: " + bobbing.getPosition(i));
        }
    }
}
