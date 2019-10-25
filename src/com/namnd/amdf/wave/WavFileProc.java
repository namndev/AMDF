/**
 * <p>Copyright: Copyright (c) 2012</p>
 * @version 1.0
 */
package com.namnd.amdf.wave;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author namnd
 * @mobile 0986001325
 * @email: dinhnam.yt@gmail.com
 * @Date: Thursday, October 11, 2012
 */
public class WavFileProc {

	private static final int NUM_BITS_PER_BYTE = 8;
	private int[] value;
	private WavInfo wavInfo;

	public ArrayList<Integer> sData;
	DataInputStream dataInputStream;
	private int[][] samples;
	int max = 0;
	int min = 0;
	double biggest;
	// kich thuoc data of wav
	public byte bytes[];

	public WavFileProc(File file) {
		sData = new ArrayList<Integer>();
		wavInfo = new WavInfo(file.getName());
		try {
			dataInputStream = new DataInputStream(new FileInputStream(file));
			loadWavInfo();
			samples = getByteArray();
			if (max > min)
				biggest = max;
			else
				biggest = Math.abs((double) min);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public WavInfo getWavInfo() {
		return wavInfo;
	}

	/**
	 * get information from wav file
	 */
	private void loadWavInfo() {
		value = null;
		try {
			int read = 0;
			int count = 0;
			String s = "";
			int index = 0;
			while (((read = dataInputStream.read()) != -1) && count < 45) {
				count++;
				// desc="count tu 1 den 44">
				// chunkID: 4byte
				if (count >= 1 && count <= 4) {
					s += (char) read;
					if (count == 4) {
						wavInfo.chunkID = s;
						s = "";
					}
				}
				if (count >= 5 && count <= 8) {
					if (value == null) {
						value = new int[4];
					}
					if (value != null) {
						value[index] = read;
						index++;
					}
					s = s + getHexFromDec(read);
					if (count == 8) {
						s += "  "
								+ readLittleShort(value[0], value[1], value[2],
										value[3]);
						wavInfo.chunkSize = readLittleShort(value[0], value[1],
								value[2], value[3]);
						value = null;
						s = "";
						index = 0;
					}
				}
				if (count >= 9 && count <= 12) {
					s = s + (char) read;
					if (count == 12) {
						wavInfo.format = s;
						s = "";
					}
				}
				if (count >= 13 && count <= 16) {
					s = s + (char) read;
					if (count == 16) {
						wavInfo.subChunk1ID = s;
						s = "";
					}
				}
				if (count >= 17 && count <= 20) {
					if (value == null) {
						value = new int[4];
					}
					if (value != null) {
						value[index] = read;
						index++;
					}
					s = s + " " + getHexFromDec(read);
					if (count == 20) {
						s += "  "
								+ readLittleShort(value[0], value[1], value[2],
										value[3]);
						wavInfo.subChunk1Size = (int) readLittleShort(value[0],
								value[1], value[2], value[3]);
						s = "";
						value = null;
						index = 0;
					}
				}
				if (count >= 21 && count <= 22) {
					if (value == null) {
						value = new int[2];
					}
					if (value != null && index < 2) {
						value[index] = read;
						index++;
					}
					s = s + " " + getHexFromDec(read);
					if (count == 22) {
						s += "  " + readLittleShort(value[0], value[1]);
						wavInfo.audioFormat = (int) readLittleShort(value[0],
								value[1]);
						s = "";
						value = null;
						index = 0;
					}
				}
				if (count >= 23 && count <= 24) {
					if (value == null) {
						value = new int[2];
					}
					if (value != null) {
						value[index] = read;
						index++;
					}
					s = s + " " + getHexFromDec(read);
					if (count == 24) {
						s += "  " + readLittleShort(value[0], value[1]);
						wavInfo.numChanel = (int) readLittleShort(value[0],
								value[1]);
						s = "";
						value = null;
						index = 0;
					}
				}
				if (count >= 25 && count <= 28) {
					if (value == null) {
						value = new int[4];
					}
					if (value != null) {
						value[index] = read;
						index++;
					}
					s += " " + getHexFromDec(read);
					if (count == 28) {
						s += "  "
								+ readLittleShort(value[0], value[1], value[2],
										value[3]);
						wavInfo.sampleRate = readLittleShort(value[0],
								value[1], value[2], value[3]);
						s = "";
						value = null;
						index = 0;
					}
				}
				if (count >= 29 && count <= 32) {
					if (value == null) {
						value = new int[4];
					}
					if (value != null) {
						value[index] = read;
						index++;
					}
					s = s + " " + getHexFromDec(read);
					if (count == 32) {
						s += "  "
								+ readLittleShort(value[0], value[1], value[2],
										value[3]);
						wavInfo.byteRate = readLittleShort(value[0], value[1],
								value[2], value[3]);
						s = "";
						value = null;
						index = 0;
					}
				}
				if (count >= 33 && count <= 34) {
					if (value == null) {
						value = new int[2];
					}
					if (value != null) {
						value[index] = read;
						index++;
					}
					s = s + " " + getHexFromDec(read);
					if (count == 34) {

						s += "  " + readLittleShort(value[0], value[1]);
						wavInfo.blockAlign = (int) readLittleShort(value[0],
								value[1]);
						s = "";
						value = null;
						index = 0;
					}
				}
				if (count >= 35 && count <= 36) {
					if (value == null) {
						value = new int[2];
					}
					if (value != null) {
						value[index] = read;
						index++;
					}
					s = s + " " + getHexFromDec(read);
					if (count == 36) {
						s += "  " + readLittleShort(value[0], value[1]);
						wavInfo.bitPerSample = (int) readLittleShort(value[0],
								value[1]);
						s = "";
						value = null;
						index = 0;
					}
				}
				if (count >= 37 && count <= 40) {
					s = s + (char) read;
					if (count == 40) {
						wavInfo.subChunk2ID = s;
						s = "";
					}
				}
				if (count >= 41 && count <= 44) {
					if (value == null) {
						value = new int[4];
					}
					if (value != null) {
						value[index] = read;
						index++;
					}
					s = s + " " + getHexFromDec(read);
					if (count == 44) {
						s += "  "
								+ readLittleShort(value[0], value[1], value[2],
										value[3]);
						wavInfo.subChunk2Size = (int) readLittleShort(value[0],
								value[1], value[2], value[3]);
						s = "";
						value = null;
						index = 1;
					}
				}
			} // end while

			int LENGHT = (int) (wavInfo.chunkSize - wavInfo.subChunk1Size - 20);
			// LENGHT = subChunk2Size
			bytes = new byte[LENGHT];
			dataInputStream.read(bytes);
			dataInputStream.close();
		} catch (IOException ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
		}
	} // end function

	private int[][] getByteArray() {
		int numChanel = wavInfo.numChanel;
		int size = bytes.length;
		int[][] toReturn = new int[numChanel][size / (numChanel * 2)];
		int index = 0;
		// loop through the byte[]
		for (int i = 0; i < size;) {
			// moi vong lap, lap di lap lai thong qua cac kenh
			for (int j = 0; j < numChanel; j++) {
				int low = (int) bytes[i];
				i++;
				int high = (int) bytes[i];
				i++;
				int sample = (high << NUM_BITS_PER_BYTE) + (low & 0x00ff);
				if (sample < min)
					min = sample;
				else if (sample > max)
					max = sample;
				toReturn[j][index] = sample;
			}
			index++;
		}
		return toReturn;
	}

	public int[] getSamples(int chanel) {
		return samples[chanel];
	}

	public long readLittleShort(int ch1, int ch2) {
		int low = ch1 & 0xff;
		int high = ch2 & 0xff;
		return (int) (high << 8 | low);
	}

	public long readLittleShort(int ch1, int ch2, int ch3, int ch4) {
		byte[] tmp = new byte[4];
		tmp[0] = (byte) ch1;
		tmp[1] = (byte) ch2;
		tmp[2] = (byte) ch3;
		tmp[3] = (byte) ch4;
		long accum = 0;
		int i = 0;
		for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
			accum |= ((long) (tmp[i] & 0xff)) << shiftBy;
			i++;
		}
		return accum;
	}

	public String getHexFromDec(int dec) {
		String s = "";
		int a = dec / 16;
		int b = dec % 16;
		if (a == 0) {
			s = "0" + getHexValue(b);
		} else {

			while (a > 0) {
				s = getHexValue(b) + s;
				b = a % 16;
				a = a / 16;
			}
			s = getHexValue(b) + s;
		}
		return s;
	}

	public String getHexValue(int a) {
		String s = "";
		if (a >= 0 && a <= 9) {
			s = "" + a;
		} else {
			if (a == 10) {
				s = "A";
			} else if (a == 11) {
				s = "B";
			} else if (a == 12) {
				s = "C";
			} else if (a == 13) {
				s = "D";
			} else if (a == 14) {
				s = "E";
			} else if (a == 15) {
				s = "F";
			}
		}

		return s;
	}

	public int getDecValue(String s) {
		int a = 0;
		if (s.equalsIgnoreCase("0")) {
			a = 0;
		} else if (s.equalsIgnoreCase("1")) {
			a = 1;
		} else if (s.equalsIgnoreCase("2")) {
			a = 2;
		} else if (s.equalsIgnoreCase("3")) {
			a = 3;
		} else if (s.equalsIgnoreCase("4")) {
			a = 4;
		} else if (s.equalsIgnoreCase("5")) {
			a = 5;
		} else if (s.equalsIgnoreCase("6")) {
			a = 6;
		} else if (s.equalsIgnoreCase("7")) {
			a = 7;
		} else if (s.equalsIgnoreCase("8")) {
			a = 8;
		} else if (s.equalsIgnoreCase("9")) {
			a = 9;
		} else if (s.equalsIgnoreCase("A")) {
			a = 10;
		} else if (s.equalsIgnoreCase("B")) {
			a = 11;
		} else if (s.equalsIgnoreCase("C")) {
			a = 12;
		} else if (s.equalsIgnoreCase("D")) {
			a = 13;
		} else if (s.equalsIgnoreCase("E")) {
			a = 14;
		} else if (s.equalsIgnoreCase("F")) {
			a = 15;
		}
		return a;
	}
}
