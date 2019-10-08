package com.git.template.general.excel.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class ExcelUtils {
	
	Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
	
	/**
	 * 一些默认的配置,可以使用springboot的Configuration在配置文件中将其转换为配置
	 */
	private static final String charset = "utf-8";
	// 是否启用严格模式,默认设置为false
	private static final boolean isStrict = false;
	
	/**
	 * @param workbook 需要先生成workbook,然后生成sheet
	 * @param data 该sheet中存储的数据
	 * @param isStrict 是否使用严格模式,该模式会遍历所有的data,以找出其中所有的excel列标题
	 * 			如果该参数为false,则只会遍历第一个Map,将其中的键作为列标题
	 * @return 包含数据的Sheet
	 */
	private static<T> Sheet getSheet(Workbook workbook,List<Map<String,Object>> data,boolean isStrict){
		Sheet sheet = workbook.createSheet();
		List<String> fieldNames;
		// 如果没有数据,则直接返回一个空的sheet;
		if(data.size()==0 || data.get(0).size()==0) return sheet;
		//先遍历整个List,得到所有的键,即表格的标题(效率较低,但是如果某个数据中,不一定包含全部的列,则需要使用该方法,否则会导致该列缺失,并会引起index为-1的报错)
		if(isStrict){
			Set<String> fieldSet = new LinkedHashSet<String>();
			for(Map<String,Object> map : data){
				for(Map.Entry<String,Object> entry:map.entrySet()){
					fieldSet.add(entry.getKey());
				}
			}
			fieldNames = new ArrayList<>(fieldSet);
		//如果List中的每一个Map结构一致,则可以不使用严格模式,直接以第一个Map中的键作为标题
		}else{
			Map<String, Object> map = data.get(0);
			fieldNames = new ArrayList<String>();
			for(Map.Entry<String,Object> entry:map.entrySet()){
				fieldNames.add(entry.getKey());
			}
		}
		//生成列标题
		Row firstRow = sheet.createRow(0);
		for(int i=0;i<fieldNames.size();i++){
			Cell cell = firstRow.createCell(i);
			cell.setCellValue(fieldNames.get(i));
		}
		
		//遍历数据集合,生成rows和cells,返回sheet
		int rowFlag = 1;
		for(Map<String,Object> map : data){
			Row row = sheet.createRow(rowFlag++);
			for(Map.Entry<String,Object> entry:map.entrySet()){
				Cell cell = row.createCell(fieldNames.indexOf(entry.getKey()));
				cell.setCellValue(entry.getValue().toString());
			}
		}
		return sheet;
	}
	private static Sheet getSheet(Workbook workbook,List<Map<String,Object>> data){
		return getSheet(workbook,data,isStrict);
	}	
	
	// 遍历所有的数据集合,然后生成一个workbook
	public static Workbook getWorkbook(List<Map<String, Object>>[] datas) {
		Workbook workbook = new XSSFWorkbook();
		for(int i=0;i<datas.length;i++){
			if(datas[i].size()!=0 && datas[i].get(0).size()!=0){
				getSheet(workbook,datas[i]);
			}
		}
		return workbook;
	}
	

	public static Workbook getWorkbook(List<Map<String, Object>> data) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>>[] datas = new List[1];
		datas[0] = data;
		return getWorkbook(datas);
	}
	
	
	
	
	
/**=================== 
 * 
 * 以下代码应该作为另一个类的一部分
 * 
 *  ============================**/
	
	private static final String[] prefixs = {"auth","bank","cup","qpay","rtlloan"};
	private static final Pattern pattern = Pattern.compile("rule\\s*\"\\[(\\w*)\\]\\s*(.*?)\"");
	// 是否遍历子文件夹,将其默认设置为false
	private static final boolean includeChildDir = false;
	
	/**
	 * 该方法应该是作为另外一个实现类的方法,ExcelUtils类除了生成Excel,不应该涉及文件相关操作
	 * @param dir
	 * @param includeChildDir
	 * @return
	 */
	// 遍历某个文件夹下所有这些前缀的文件,第二个参数代表是否遍历所有子文件夹
	private static List<Map<String,Object>>[] dirToData(File dir,boolean includeChildDir){
		if(dir==null || !dir.exists() || !dir.isDirectory()) throw new RuntimeException("传入的不是一个有效的文件夹");
		
		@SuppressWarnings("unchecked")
		List<Map<String,Object>>[] list = new List[prefixs.length];
		for(int i = 0;i<prefixs.length;i++){
			list[i] = new ArrayList<Map<String,Object>>();
		}
		
		// 以下写法需要jdk1.8,如果1.8以下,需要使用 Arrays.asList(prefixs);
		List<Object> prefixList = Arrays.stream(prefixs).collect(Collectors.toList());
		dirToData$(dir,prefixList,list,includeChildDir);
		return list;
	}
	private static List<Map<String,Object>>[] dirToData(File dir){
		return dirToData(dir,includeChildDir);
	}
	
	private static void dirToData$(File dir,List<Object> prefixList,List<Map<String,Object>>[] dataList,boolean includeChildDir){
		for(String childName : dir.list()){
			File child = new File(dir,childName);
			if(childName.indexOf("-")!=-1){
				String childPrefix = childName.substring(0,childName.indexOf("-"));
				if(prefixList.contains(childPrefix) && !child.isDirectory()){
					int index = prefixList.indexOf(childPrefix);
					fillData(dataList[index],child);
				}
			}
			if(child.isDirectory() && includeChildDir){
				dirToData$(child,prefixList,dataList,includeChildDir);
			}
		}
	}
	
	private static void fillData(List<Map<String, Object>> list, File child) {
		try (
				BufferedReader br= new BufferedReader(
						new InputStreamReader(new FileInputStream(child),charset));	
				){
			String line;
			while((line=br.readLine())!=null){
				Matcher matcher = pattern.matcher(line);
				if(matcher.find()){
					Map<String,Object> map = new LinkedHashMap<>();
					map.put("校验点", matcher.group(1));
					map.put("文件名称", child.getName());
					map.put("中文描述", matcher.group(2));
					list.add(map);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 该方法将某个文件夹下的所有对应的文件转换成一个excel输出文件
	 * @param filePath 需要转换的文件所在的文件夹
	 * @param outpath 输出文件的路径
	 * @throws IOException
	 */
	public static void txtToExcel(String filePath,String outpath) throws IOException{
		List<Map<String, Object>>[] datas = dirToData(new File(filePath));
		Workbook workbook = getWorkbook(datas);
		OutputStream s = new FileOutputStream(outpath);
		workbook.write(s);
		workbook.close();
	}
	
	
	
	/** =========== 以下为本次新添加的方法  ================= **/
	
	
	/**
	 * 该方法的排序与数据文件的读取顺序有关
	 * @param originDir 数据所在的文件夹,默认情况下,不会遍历子文件夹
	 * @param checkPointFile 校验点文件,该文件记录了所有会被导出为excel的校验点
	 * @param targetFile 生成的excel文件全路径
	 * @throws IOException
	 */
	public static void toExcel(String originDir,String checkPointFile,String targetFile) throws IOException{
		// 先得到checkPointFile的所有内容,作为一个数组,在得到内容后,据此作为过滤条件
		List<String> lines = getFileByLine(new File(checkPointFile));
		// 在遍历originDir,以将匹配所有的pointFile的内容
		// 为避免重写,复用原本的方法,稍作转换
		List<Map<String, Object>>[] datas = dirToData(new File(originDir));
		
		List<Map<String,Object>> data = new ArrayList<>();
		for (List<Map<String, Object>> list : datas) {
			for (Map<String, Object> map : list) {
				if(lines.contains(map.get("校验点").toString())){
					data.add(map);
				}
			}
		}
		// 生成excel
		Workbook workbook = new XSSFWorkbook();
		getSheet(workbook,data);
		OutputStream s = new FileOutputStream(targetFile);
		workbook.write(s);
		workbook.close();
	}
	
	/**
	 * 该方法的排序与校验点文件相同,实际做法与上面的方法并无区别,只是在最后根据校验点文件进行了排序
	 * @param originDir 数据所在的文件夹,默认情况下,不会遍历子文件夹
	 * @param checkPointFile 校验点文件,该文件记录了所有会被导出为excel的校验点
	 * @param targetFile 生成的excel文件全路径
	 * @throws IOException
	 */
	public static void toExcelOrderByCheckPoint(String originDir,String checkPointFile,String targetFile) throws IOException{

		List<String> lines = getFileByLine(new File(checkPointFile));
		List<Map<String, Object>>[] datas = dirToData(new File(originDir));
		List<Map<String,Object>> data = new ArrayList<>();
		
		for (List<Map<String, Object>> list : datas) {
			for (Map<String, Object> map : list) {
				if(lines.contains(map.get("校验点").toString())){
					data.add(map);
				}
			}
		}
		
		// System.out.println(lines.stream().filter((str)->{if(str.startsWith("Cro")) return true; return false;}).collect(Collectors.toList()));
		
		Set<Map<String,Object>> set = new TreeSet<>((map1,map2)->{
			int index1 = lines.indexOf(map1.get("校验点").toString());
			int index2 = lines.indexOf(map2.get("校验点").toString());
			if(index1-index2>0) return 1;
			else return -1;
		});
		set.addAll(data);
		
		List<Map<String,Object>> list = new ArrayList<>(set);
		
		// 生成excel
		Workbook workbook = new XSSFWorkbook();
		getSheet(workbook,list);
		OutputStream s = new FileOutputStream(targetFile);
		workbook.write(s);
		workbook.close();
	}
	
	/** ============= 以下为读取文件的方法  ============= **/
	public static List<String> getFileByLine(File file,String charset){
		return getFileByLine(file,charset,true);
	}
	public static List<String> getFileByLine(File file,boolean trim){
		return getFileByLine(file,"utf-8",trim);
	}
	public static List<String> getFileByLine(File file){
		return getFileByLine(file,"utf-8");
	}
	public static List<String> getFileByLine(File file,String charset,boolean trim){
		checkFile(file);
		List<String> list = new ArrayList<>();
		
		try(
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
						new FileInputStream(file),charset));){
			String line;
			while((line=br.readLine())!=null){
				if(trim) list.add(line.trim());
				else list.add(line);
			}
			return list;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void checkFile(File file){
		if(file==null)
			throw new RuntimeException("文件为空");
		if(!file.exists())
			throw new RuntimeException("文件不存在");
		if(file.isDirectory())
			throw new RuntimeException("需要文件,但传入的是文件夹"+file.getAbsolutePath());
		
	}
	
	/** ============= 以上为读取文件的方法,这些方法原本应该拆分为为另一个Utils,为避免产生相关依赖,将相关方法复制进本类中 ============= **/
	
	
	// 测试用的main方法
	public static void main(String[] args) throws IOException {
		// txtToExcel("d:/", "d:/excel.xlsx");
		// toExcel("d:/","d:/取现模板校验点.txt","d:/excel.xlsx");
		toExcelOrderByCheckPoint("d:/","d:/取现模板校验点.txt","d:/excel.xlsx");
	}
	
	
}