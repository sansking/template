package com.git.template.general.excel;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExcelUtils{
	
	Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
	
	/**
	 * 一些默认的配置,可以使用springboot的Configuration在配置文件中将其转换为配置
	 */
	private static final String[] prefixs = {"auth","bank","cup","qpay","rtlloan"};
	private static final String charset = "utf-8";
	private static final Pattern pattern = Pattern.compile("rule\\s*\"\\[(\\w*)\\]\\s*(.*?)\"");
	// 是否遍历子文件夹,将其默认设置为false
	private static final boolean includeChildDir = false;
	// 是否启用严格模式,默认设置为false
	private static final boolean isStrict = false;
	
	/**
	 * 
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
	
	
	// 遍历某个文件夹下所有这些前缀的文件,第二个参数代表是否遍历所有子文件夹
	private static List<Map<String,Object>>[] dirToData(File dir,boolean includeChildDir){
		if(dir==null || !dir.exists() || !dir.isDirectory()) throw new RuntimeException("传入的不是一个有效的文件夹");
		
		@SuppressWarnings("unchecked")
		List<Map<String,Object>>[] list = new List[prefixs.length];
		for(int i = 0;i<prefixs.length;i++){
			list[i] = new ArrayList<Map<String,Object>>();
		}
		List<Object> prefixList = Arrays.asList(prefixs);
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

	private static Workbook getWorkbook(String filePath){
		File f = new File(filePath);
		return getWorkbook(f);
	}
	
	// 遍历所有的数据集合,然后生成一个workbook
	private static 	Workbook getWorkbook(File f) {
		@SuppressWarnings("resource")
		Workbook workbook = new XSSFWorkbook();
		List<Map<String, Object>>[] datas = dirToData(f);
		for(int i=0;i<datas.length;i++){
			if(datas[i].size()!=0 && datas[i].get(0).size()!=0){
				getSheet(workbook,datas[i]);
			}
		}
		return workbook;
	}
	
	/**
	 * 
	 * 该方法将某个文件夹下的所有对应的文件转换成一个excel输出文件
	 * @param filePath 需要转换的文件所在的文件夹
	 * @param outpath 输出文件的路径
	 * @throws IOException
	 */
	public static void txtToExcel(String filePath,String outpath) throws IOException{
		Workbook workbook = getWorkbook(filePath);
		OutputStream s = new FileOutputStream(outpath);
		workbook.write(s);
		workbook.close();
	}
	
	// 测试用的main方法
	public static void main(String[] args) throws IOException {
		txtToExcel("d:/", "d:/excel.xlsx");
	}
	
	
}
