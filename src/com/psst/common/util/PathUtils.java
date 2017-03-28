package com.psst.common.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * 这个类提供了一些根据类的class文件位置来定位的方法。
 */
public class PathUtils {
	/**
	 * 获取一个类的class文件所在的绝对路径。
	 * @param cls
	 * 			, 对象的Class属性
	 */
	public static String getPathFromClass(Class<?> cls) throws IOException {
		String path = null;
		if (cls == null) {
			throw new NullPointerException("参数不能为空。");
		}
		URL url = getClassLocationURL(cls);
		if (url != null) {
			path = url.getPath();
			if ("jar".equalsIgnoreCase(url.getProtocol())) {
				try {
					path = new URL(path).getPath();
				} catch (MalformedURLException e) {
				}
				int location = path.indexOf("!/");
				if (location != -1) {
					path = path.substring(0, location);
				}
			}
			File file = new File(path);
			path = file.getCanonicalPath();
		}
		return path;
	}

	/**
	 * 这个方法可以通过与某个类的class文件的相对路径来获取文件或目录的绝对路径。 通常
	 * 在程序中很难定位某个相对路径，特别是在B/S应用中。通过这个方法，我们可以根据我们 程序自身的类文件的位置来定位某个相对路径。
	 * 
	 * 
	 * @param relatedPath
	 *            ，相对路径
	 * @param cls
	 *            ，用来定位的类
	 * @return 相对路径所对应的绝对路径
	 * @throws IOException
	 *             因为本方法将查询文件系统，所以可能抛出IO异常
	 */
	public static String getFullPathRelateClass(String relatedPath, Class<?> cls)
			throws IOException {
		String path = null;
		if (relatedPath == null) {
			throw new NullPointerException("参数不能为空。");
		}
		String clsPath = getPathFromClass(cls);
		File clsFile = new File(clsPath);
		String tempPath = clsFile.getParent() + File.separator + relatedPath;
		File file = new File(tempPath);
		path = file.getCanonicalPath();
		return path;
	}

	/**
	 * 获取类的class文件位置的URL。这个方法是本类最基础的方法，供其它方法调用。
	 */
	private static URL getClassLocationURL(final Class<?> cls) {
		if (cls == null)
			throw new NullPointerException("参数不能为空。");
		URL result = null;
		final String clsAsResource = cls.getName().replace('.', '/').concat(
				".class");
		final ProtectionDomain pd = cls.getProtectionDomain();
		// java.lang.Class contract does not specify
		// if 'pd' can ever be null;
		// it is not the case for Sun's implementations,
		// but guard against null
		// just in case:
		if (pd != null) {
			final CodeSource cs = pd.getCodeSource();
			// 'cs' can be null depending on
			// the classloader behavior:
			if (cs != null)
				result = cs.getLocation();

			if (result != null) {
				// Convert a code source location into
				// a full class file location
				// for some common cases:
				if ("file".equals(result.getProtocol())) {
					try {
						if (result.toExternalForm().endsWith(".jar")
								|| result.toExternalForm().endsWith(".zip")) {
							result = new URL("jar:".concat(
									result.toExternalForm()).concat("!/")
									.concat(clsAsResource));
						} else if (new File(result.getFile()).isDirectory()) {
							result = new URL(result, clsAsResource);
						}
					} catch (MalformedURLException ignore) {
					}
				}
			}
		}

		if (result == null) {
			// Try to find 'cls' definition as a resource;
			// this is not
			// document．d to be legal, but Sun's
			// implementations seem to //allow this:
			final ClassLoader clsLoader = cls.getClassLoader();
			result = clsLoader != null ? clsLoader.getResource(clsAsResource)
					: ClassLoader.getSystemResource(clsAsResource);
		}
		return result;
	}
	
	/**
	 * @Description 从源路径下获得所有文件
	 * @param srcPath 源路径
	 * @param needRec 是否递归
	 * @param containDir 返回结果是否包含下级路径
	 * @return
	 */
	public static List<File> searchFilesFromPath(File srcPath, boolean needRec, boolean containDir) {
		//目录不存在
		if (!srcPath.exists()) {
			return null;
		}
		List<File> resultFilesList = new ArrayList<File>();
		if (!srcPath.isDirectory()) {
			resultFilesList.add(srcPath);
			return resultFilesList;
		}
		//不递归
		if (!needRec) {
			File[] files = srcPath.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					//返回路径
					if (containDir) {
						resultFilesList.add(f);
					}
				}
				else {
					resultFilesList.add(f);
				}
			}
		}
		//递归
		else {
			List<File> dirs = new ArrayList<File>();
			dirs.add(srcPath);
			while (dirs.size() > 0) {
				File dirFile = dirs.remove(dirs.size() - 1);
				File[] files = dirFile.listFiles();
				for (File f : files) {
					if (f.isDirectory()) {
						dirs.add(f);
						if (containDir) {
							resultFilesList.add(f);
						}
					}
					else {
						resultFilesList.add(f);
					}
				}
			}
		}
		return resultFilesList;
	}
	
	/**
	 * @Description 目录扫描
	 * @param srcPath 源目录
	 * @param destPath 目的目录
	 * @param fileNeedDel 扫完是否删除文件
	 * @param needRec 是否递归
	 * @param needDecode 是否解码
	 * @return 解码成功列表
	 */
	public static List<String> fileCopy(File srcPath, File destPath, boolean fileNeedDel,
			boolean needRec, boolean needDecode) {
		//获得文件列表
		List<File> files = searchFilesFromPath(srcPath, needRec, false);
		List<String> successList = new ArrayList<String>();
		if (!destPath.exists()) {
			destPath.mkdir();
		}
		for (File f : files) {
			//需要解码
			if (needDecode) {
				boolean decodeResult = decode(f, destPath);
				//成功了
				if (decodeResult) {
					successList.add(f.getName());
				}
			}
			//无需解码
			else {
				try {
					FileUtils.copyFileToDirectory(f, destPath);
					successList.add(f.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//需要删除
			if (fileNeedDel) {
				f.delete();
			}
		}
		return successList;
	}
	
	/**
	 * @Description 解码程序
	 * @param srcFile 源文件
	 * @param destPath 解码后放置目录
	 * @return 解码是否成功
	 */
	public static boolean decode(File srcFile, File destPath) {
		if (srcFile.isDirectory() || !destPath.isDirectory() || !srcFile.exists()) {
			return false;
		}
		if (!destPath.exists()) {
			destPath.mkdir();
		}
		return true;
	}
	
}