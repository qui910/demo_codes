package com.duoduo.demo.jsip.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;

/**
 * 文件操作帮助类
 * @author chengesheng
 * @date 2011-7-14 下午06:37:06
 */

public class FileUtil {

	private static String FILE_SEPARATOR = "/";

	/**
	 * 新建目录
	 * @param path String 如 c:/fqf
	 * @return boolean
	 */
	public static boolean createFolder(String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 新建文件
	 * @param filePath String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent String 文件内容
	 * @return boolean
	 */
	public static boolean createFile(String filePath, String fileContent) {
		try {
			// 文件所在目录不存在则创建
			int find = filePath.lastIndexOf(FILE_SEPARATOR);
			if (find != -1) {
				String path = filePath.substring(0, find);
				createFolder(path);
			}

			// 文件不存在则创建
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}

			// 写入文件内容
			FileWriter resultFile = new FileWriter(file);
			PrintWriter myFile = new PrintWriter(resultFile);
			myFile.println(fileContent);
			myFile.close();
			resultFile.close();
			// OutputStreamWriter方式
			// OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("hello2.txt"));
			// osw.write(s, 0, s.length());
			// osw.flush();
			// osw.close();
			// PrintWriter方式
			// PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("hello3.txt")), true);
			// pw.println(s);
			// pw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除文件
	 * @param filePath String 文件路径及名称 如c:/fqf.txt
	 * @return boolean
	 */
	public static boolean deleteFile(String filePath) {
		try {
			File file = new File(filePath);
			file.delete();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除文件夹
	 * @param folderPath String 文件夹路径及名称 如c:/fqf
	 * @return boolean
	 */
	public static boolean deleteFolder(String folderPath) {
		try {
			return deleteAllFile(folderPath) && deleteFile(folderPath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * @param folderPath String 文件夹路径 如 c:/fqf
	 * @return boolean
	 */
	public static boolean deleteAllFile(String folderPath) {
		File file = new File(folderPath);
		// 路径不存在则返回false
		if (!file.exists()) {
			return false;
		}
		// 指定路径不是目录则返回false
		if (!file.isDirectory()) {
			return false;
		}

		// 递归删除目录下的所有文件和子目录
		String[] files = file.list();
		for (int i = 0; i < files.length; i++) {
			String path = null;
			if (folderPath.endsWith(FILE_SEPARATOR)) {
				path = folderPath + files[i];
			} else {
				path = folderPath + FILE_SEPARATOR + files[i];
			}
			file = new File(path);
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				// 先删除子文件夹
				deleteFolder(path);
			}
		}
		return true;
	}

	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static boolean copyFile(String oldPath, String newPath) {
		try {
			File oldfile = new File(oldPath);
			// 原文件不存在则返回false
			if (!oldfile.exists()) {
				return false;
			}

			// 文件所在目录不存在则创建
			int find = newPath.lastIndexOf(FILE_SEPARATOR);
			if (find != -1) {
				String path = newPath.substring(0, find);
				createFolder(path);
			}

			// 拷贝文件内容
			FileInputStream fis = new FileInputStream(oldPath); // 读入原文件
			FileOutputStream fos = new FileOutputStream(newPath);
			byte[] buffer = new byte[1024];
			int byteread = 0;
			while ((byteread = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, byteread);
			}
			fos.flush();
			fos.close();
			fis.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 复制整个文件夹内容
	 * @param oldPath String 原文件路径 如：c:/fqf
	 * @param newPath String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static boolean copyFolder(String oldPath, String newPath) {
		try {
			File folder = new File(oldPath);
			// 如果原文件夹不存在 则返回false
			if (!folder.exists()) {
				return false;
			}

			// 如果目标文件夹不存在 则建立新文件夹
			createFolder(newPath);

			// 循环拷贝目录下的所有子目录和文件
			String[] files = folder.list();
			for (int i = 0; i < files.length; i++) {
				String path = null;
				if (oldPath.endsWith(FILE_SEPARATOR)) {
					path = oldPath + files[i];
				} else {
					path = oldPath + FILE_SEPARATOR + files[i];
				}

				File file = new File(path);
				if (file.isFile()) {
					// 拷贝文件
					FileInputStream fis = new FileInputStream(file);
					FileOutputStream fos = new FileOutputStream(newPath + FILE_SEPARATOR + file.getName());
					byte[] b = new byte[1024];
					int len;
					while ((len = fis.read(b)) != -1) {
						fos.write(b, 0, len);
					}
					fos.flush();
					fos.close();
					fis.close();
				} else if (file.isDirectory()) {
					// 若是目录则递归拷贝目录内容
					if (oldPath.endsWith(FILE_SEPARATOR)) {
						copyFolder(oldPath + files[i], newPath + files[i]);
					} else {
						copyFolder(oldPath + FILE_SEPARATOR + files[i], newPath + FILE_SEPARATOR + files[i]);
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移动文件到指定目录
	 * @param oldPath String 如：c:/fqf.txt
	 * @param newPath String 如：d:/fqf.txt
	 * @return boolean
	 */
	public static boolean moveFile(String oldPath, String newPath) {
		return copyFile(oldPath, newPath) && deleteFile(oldPath);
	}

	/**
	 * 移动文件到指定目录
	 * @param oldPath String 如：c:/fqf.txt
	 * @param newPath String 如：d:/fqf.txt
	 * @return boolean
	 */
	public static boolean moveFolder(String oldPath, String newPath) {
		return copyFolder(oldPath, newPath) && deleteFolder(oldPath);
	}
	
	/**
	 * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
	 * @param fileName 文件的名
	 */
	public static void readFileByBytes(String fileName) {
		InputStream in = null;
		try {
			in = new FileInputStream(fileName);
			// 一次读多个字节
			byte[] tempbytes = new byte[1024];
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			int byteread = 0;
			while ((byteread = in.read(tempbytes)) != -1) {
				System.out.write(tempbytes, 0, byteread);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e1) {
				}
			}
		}
	}

	/**
	 * 以字符为单位读取文件，常用于读文本，数字等类型的文件
	 * @param fileName 文件名
	 */
	public static void readFileByChars(String fileName) {
		Reader reader = null;
		try {
			// 一次读多个字符
			char[] tempchars = new char[1024];
			int charread = 0;
			reader = new InputStreamReader(new FileInputStream(fileName));
			// 读入多个字符到字符数组中，charread为一次读取字符数
			while ((charread = reader.read(tempchars)) != -1) {
				// 同样屏蔽掉r不显示
				if ((charread == tempchars.length) && (tempchars[tempchars.length - 1] != 'r')) {
					System.out.print(tempchars);
				} else {
					for (int i = 0; i < charread; i++) {
						if (tempchars[i] == 'r') {
							continue;
						} else {
							System.out.print(tempchars[i]);
						}
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e1) {
				}
			}
		}
	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 * @param fileName 文件名
	 */
	public static String readFileByLines(String fileName) {
		StringBuffer ret = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(fileName)));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				if(ret.length()>0) {
					ret.append("\n");
				}
				ret.append(tempString);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e1) {
				}
			}
		}
		return ret.toString();
	}

	/**
	 * 随机读取文件内容
	 * @param fileName 文件名
	 */
	public static void readFileByRandomAccess(String fileName) {
		RandomAccessFile randomFile = null;
		try {
			System.out.println("随机读取一段文件内容：");
			// 打开一个随机访问文件流，按只读方式
			randomFile = new RandomAccessFile(fileName, "r");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 读文件的起始位置
			int beginIndex = (fileLength > 4) ? 4 : 0;
			// 将读文件的开始位置移到beginIndex位置。
			randomFile.seek(beginIndex);
			byte[] bytes = new byte[10];
			int byteread = 0;
			// 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
			// 将一次读取的字节数赋给byteread
			while ((byteread = randomFile.read(bytes)) != -1) {
				System.out.write(bytes, 0, byteread);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (Exception e1) {
				}
			}
		}
	}

	/**
	 * 使用RandomAccessFile追加文件内容
	 * @param fileName
	 * @param content 
	 */ 
	public static void appendByRandomAccess(String fileName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用FileWriter追加文件内容
	 * @param fileName
	 * @param content
	 */
	public static void appendByFileWriter(String fileName, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 格式化路径，路径中的"\\"处理为"/"
	 * @param path
	 * @return String
	 */
	public static String formatPath(String path) {
		if (path == null) {
			return "";
		}
		return path.replaceAll("\\\\", "/");
	}

	/**
	 * (用一句话描述方法的主要功能)
	 * @param args
	 */

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		FileUtil.createFolder("c:/test");
		System.out.println("newFolder>>" + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		FileUtil.createFile("c:/test/test11.txt", "test011,test012");
		FileUtil.createFile("c:/test/test1/test11.txt", "test11,test22");
		FileUtil.createFile("c:/test/test2/test22/test222.txt", "test111,test222");
		System.out.println("newFile>>" + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		FileUtil.moveFile("c:/test/test2/test22/test222.txt", "c:/test/test3/test33/test333.txt");
		System.out.println("moveFile>>" + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		FileUtil.moveFolder("c:/test", "c:/test1");
		System.out.println("moveFolder>>" + (System.currentTimeMillis() - start));
		

		String readFileName = "C:/temp/readFile.txt";
		FileUtil.readFileByBytes(readFileName);
		FileUtil.readFileByChars(readFileName);
		FileUtil.readFileByLines(readFileName);
		FileUtil.readFileByRandomAccess(readFileName);

		String writeFileName = "C:/temp/writeFile.txt";
		String content = "new append!";
		// 使用RandomAccessFile追加文件内容
		FileUtil.appendByRandomAccess(writeFileName, content);
		FileUtil.appendByRandomAccess(writeFileName, "append end. n");
		// 显示文件内容
		FileUtil.readFileByLines(writeFileName);
		// 使用FileWriter追加文件内容
		FileUtil.appendByFileWriter(writeFileName, content);
		FileUtil.appendByFileWriter(writeFileName, "append end. n");
		// 显示文件内容
		FileUtil.readFileByLines(writeFileName);
	}
}
