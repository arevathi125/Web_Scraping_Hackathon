package Utilities;

import java.util.ArrayList;
import java.util.List;

public class ElimList {
 public static void main(String[] args) {
	List<String> list1 = new ArrayList<>();
	List<String> list2 = new ArrayList<>();
	list1.add("aa");
	list1.add("bb");
	list2.add("aa");
	list2.add("dd");
	System.out.println("list1 : "+list1);
	System.out.println("list1 : "+list2);
	list1.retainAll(list2);
	System.out.println("list1 : "+list1);
}
}
