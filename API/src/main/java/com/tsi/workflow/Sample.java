package com.tsi.workflow;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class Sample {

    public static void main(String[] args) throws ParseException, IOException {

	Map<String, Integer> words = new HashMap<>();
	words.put("hello", 3);
	words.put("world", 4);
	words.computeIfPresent("hello", (k, v) -> v + 1);
	System.out.println(words.get("hello"));
	String main = "git";

	String lQuer22y = "select imp.id from checkout_segments seg, checkout_segments seg1, system_load load1, system_load load2, implementation imp" + " where" + " seg.imp_id like ?" + " and seg.file_name = seg1.file_name" + " and seg.plan_id <> seg1.plan_id" + " and seg.target_system = seg1.target_system"
	// + " -- and seg.file_hash_code = seg1.file_hash_code"
		+ " and load1.id = seg.system_load" + " and load2.id = seg1.system_load" + " and seg.imp_id != seg1.imp_id" + " and imp.id = seg1.imp_id" + " and imp.imp_status <> ?" + " and seg.active='Y'" + " and seg1.active='Y'" + " and load1.active='Y'" + " and load2.active='Y'" + " and load2.load_date_time < load1.load_date_time";

	System.out.println(lQuer22y);
	String segCheck = "select distinct string_agg(a.file_name,',') from checkout_segments a, imp_plan b where b.active='Y' and a.plan_id = b.id and b.full_build_dt is not null and a.last_changed_time >= b.full_build_dt and b.id = :planId ";

	System.out.println(segCheck);

	StringBuilder sb2 = new StringBuilder();
	sb2.append("select c.target_system,c.file_name, a.sub_repo_id, a.source_commit_id,a.commit_date_time, a.commit_rank from ( ");
	sb2.append(" select fl.target_system, rc.file_id, rc.sub_repo_id, rc.source_commit_id, rc.commit_date_time, rank() over (partition by rc.file_id, rc.sub_repo_id, fl.target_system order by");
	sb2.append(" rc.commit_date_time desc)as  commit_rank from ").append("git").append(".repo_commit rc, ").append("git").append(".repo_file_list fl where fl.id = rc.file_id) as a ,");
	sb2.append("( select aa.target_system, aa.file_id, aa.sub_repo_id, max(aa.commit_rank) as max_rank from (select");
	sb2.append(" fl.target_system, rc.file_id, rc.sub_repo_id, rank() over (partition by rc.file_id, rc.sub_repo_id, fl.target_system");
	sb2.append(" order by rc.commit_date_time desc)as  commit_rank from ").append("git").append(".repo_commit rc, ").append("git").append(".repo_file_list fl where fl.id = rc.file_id) as aa");
	sb2.append(" group by aa.file_id, aa.sub_repo_id, aa.target_system) as b, ").append("git").append(".repo_file_list c ");
	sb2.append(" where a.file_id = b.file_id and a.sub_repo_id = b.sub_repo_id and a.commit_rank = b.max_rank and a.target_system = b.target_system and c.id = a.file_id and c.file_name = ? ");
	sb2.append(" and a.sub_repo_id =? and a.commit_rank <=3 order by a.commit_rank desc ");
	System.out.println(sb2.toString());

	String buildEnable = "select distinct string_agg(a.file_name,',') from checkout_segments a, imp_plan b where b.active='Y' and a.plan_id = b.id and b.full_build_dt is not null and a.last_changed_time >= b.full_build_dt and b.id = :planId ";
	buildEnable = buildEnable + " and a.target_system = :systemName";

	System.out.println(buildEnable);

	String sbLoadFreeze = "select plan.id as planid , string_agg(DISTINCT plan.lead_id, ', ') as leadid ,  string_agg(DISTINCT plan.lead_name, ', ') as leadname ,  string_agg(DISTINCT imp.dev_id, ', ') as developerid ,  string_agg(DISTINCT imp.dev_name, ', ') as developername, string_agg(DISTINCT plan.dev_manager, ', ') as devmanagerid, string_agg(DISTINCT plan.dev_manager_name, ', ') as devmamangername ,"
		+ " string_agg(DISTINCT plan.plan_status, ', ') as planstatus , s.name as targetsystem , load.load_date_time as loaddatetime  , fr.from_date as freezestartdate , fr.to_date as freezeenddate , fr.reason as reason " + " from system_load load , imp_plan plan , load_freeze fr , system s ,implementation imp   where load.plan_id=plan.id and fr.load_category_id=load.load_category_id " + "and  s.id = load.system_id " + "and imp.plan_id = plan.id "
		+ "and fr.active='Y' and load.active='Y' " + "and date(load.load_date_time) >= date(fr.from_date) and Date(load.load_date_time) <= date(fr.to_date) " + "and plan.plan_status in (:planStatus) " + "and load.load_category_id in (:categoryList) " + "GROUP BY plan.id , s.name , load.load_date_time , fr.from_date , fr.to_date , fr.reason ";

	System.out.println(sbLoadFreeze);
	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	String formattedTime = formatter.format(new Date());

	String hh = formattedTime.split(":")[0];
	String mm = formattedTime.split(":")[1];
	String ss = formattedTime.split(":")[2];
	// 09-26-2019 00:00:00
	String loadDateTime = "2019-11-26 17:37:52";
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	System.out.println(format.parse(loadDateTime));

	Date date = Date.from(LocalDateTime.now().minusDays(3).atZone(ZoneId.systemDefault()).toInstant());

	System.out.println(date);
	String a = "kv4i.c,kv4icd.c";
	List<String> list1 = new ArrayList<>(Arrays.asList(a));
	StringBuffer sb1 = new StringBuffer();

	for (String s2 : list1) {
	    sb1.append(s2);
	    sb1.append(",");
	}
	String str = sb1.toString();
	System.out.println(str);
	List<String> list = Stream.of(str.split(",")).collect(Collectors.toList());
	System.out.println(list);
	System.out.println("('" + StringUtils.join(list, "','") + "')");

	System.out.println(removeExtension("tpf/dl/ibm/ibm_rel19a.git"));

	System.out.println("abd" + "\r\n" + "Plan : " + "cdf");

	String lQuery = "select A.ID, B.SYSTEM_ID,A.PLAN_STATUS from imp_plan a, pre_production_loads b where a.id = b.plan_id and a.plan_status in (:planStatus) " + " and a.active='Y' and b.active='Y' and a.id in (:PlanId)";

	System.out.println(lQuery);

	String abc = "T031420.LTUA,PRE,STANDARD,SECURED,20200313,0515,TIDG0B\r\n" + "1\r\n" + "RXSNDIU: return code 1 from UTILSEGM pre 20200227 0515 tidg.asm\r\n";

	String lString = (String) abc;
	List<String> lLines = IOUtils.readLines(new StringReader(lString));

	System.out.println(lLines);

	if (lLines.size() >= 2) {
	    lLines = lLines.subList(0, lLines.size() - 2);
	}
	StringBuilder message = new StringBuilder();
	for (String temp : lLines) {
	    message.append(temp).append(",");
	}
	if (message.length() > 0) {
	    message.setLength(message.length() - 1);
	}
	System.out.println(message.toString().replace("Error Code: 8", ""));

	TimeZone tz = TimeZone.getTimeZone("EST");
	boolean inDs = tz.inDaylightTime(new Date());

	System.out.println(inDs);

	String path = "/home/E738090/projects/d1900372_001/oss";

	System.out.println(path.endsWith("projects/" + "d1900372_001" + "/" + "oss"));
	if (path.endsWith("projects/" + "d1900372_001" + "/" + "oss" + "/")) {
	    System.out.println("Loop enter");

	}

	String sbBuild = "SELECT b.plan_id as planid, " + " sys.NAME as targetsystem, " + " b.build_type as buildtype, " + " b.tdx_running_status as buildstatus, " + " CASE WHEN ( b.build_number > 0 " + " AND b.job_status IN ( 'P' ) ) THEN  to_char(b.build_date_time, 'YYYY-MM-DD HH24:MI:SS')  " + " ELSE CASE   WHEN ( b.build_number < 0 " + "  AND b.job_status IN ( 'P' ) ) THEN '' " + " END " + " END AS buildstartdate, " + "  split_part(b.jenkins_url,':', 2)    AS servername, "
		+ " CASE WHEN (b.created_dt is null) THEN '' " + " ELSE to_char(b.created_dt, 'YYYY-MM-DD HH24:MI:SS') END AS buildtriggerdate " + "FROM build b, system sys " + "WHERE  sys.id = b.system_id " + "  AND b.active = 'Y' " + "  AND b.job_status = 'P' " + " AND b.build_type IN ( 'DVL_BUILD', 'STG_BUILD' ) and b.tdx_running_status <> :RunningStatus";

	System.out.println(sbBuild);

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss.SSS");
	dateFormat.setTimeZone(TimeZone.getTimeZone("EST5EDT"));
	System.out.println(dateFormat.format(new Date()));

    }

    // Remove extension from file name
    // Ignore file names starting with .
    public static String removeExtension(String fileName) {
	if (fileName.indexOf(".") > 0) {
	    return fileName.substring(0, fileName.lastIndexOf("."));
	} else {
	    return fileName;
	}

    }

}
