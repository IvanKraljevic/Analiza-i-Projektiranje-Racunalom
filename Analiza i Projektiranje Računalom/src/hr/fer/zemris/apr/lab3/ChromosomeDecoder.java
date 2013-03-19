package hr.fer.zemris.apr.lab3;

public class ChromosomeDecoder {
	// Donja granica
	private double xMin;
	// Gornja granica
	private double xMax;
	// Broj bitova za varijablu
	private int bitNumber;
	// najveći binarni broj koji se može prikazati sa numberOfBits bitova
	private double maxBinNum;
	private int variableNumber;

	public ChromosomeDecoder(int variableNumber, int bits, double xMin,
			double xMax) {
		this.variableNumber = variableNumber;
		this.bitNumber = bits;
		this.xMin = xMin;
		this.xMax = xMax;
		this.maxBinNum = (1 << bits) - 1;
	}

	public void decodeChromosom(Chromosome c) {
		for (int i = 0; i < c.bits.length; i++) {
			int binNum = 0;
			for (int j = 0; j < c.bits[i].length; j++) {
				if (c.bits[i][j] == 1) {
					binNum += Math.pow(2, j);
				}
			}
			c.variables[i] = xMin + (double) (binNum * (xMax - xMin))
					/ (double) maxBinNum;
			if (c.variables[i] > xMax || c.variables[i] < xMin) {
				System.out.println("NEVALJA: " + c.variables[i]);
			}
		}
	}

	public double getxMin() {
		return xMin;
	}

	public void setxMin(double xMin) {
		this.xMin = xMin;
	}

	public double getxMax() {
		return xMax;
	}

	public void setxMax(double xMax) {
		this.xMax = xMax;
	}

	public int getBits() {
		return bitNumber;
	}

	public void setBits(int bits) {
		this.bitNumber = bits;
	}

	public double getMaxBinNum() {
		return maxBinNum;
	}

	public void setMaxBinNum(double maxBinNum) {
		this.maxBinNum = maxBinNum;
	}

	public int getVariableNumber() {
		return variableNumber;
	}

	public void setVariableNumber(int variableNumber) {
		this.variableNumber = variableNumber;
	}

	@Override
	public String toString() {
		return "ChromosomeDecoder [xMin=" + xMin + ", xMax=" + xMax + ", bits="
				+ bitNumber + ", maxBinNum=" + maxBinNum + ", variableNumber="
				+ variableNumber + "]";
	}

}
