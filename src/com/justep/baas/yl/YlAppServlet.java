package com.justep.baas.yl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.data.Table;
import com.justep.baas.data.Util;

public class YlAppServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String DATASOURCE_YL = "jdbc/yl";

	// Servlet入口，通过判断action参数，进入各自对应的实现方法
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		try {
			switch (action) {
			case "queryJingqu":
				queryJingqu(request, response);
				break;
			case "queryJingdian":
				queryJingdian(request, response);
				break;
			case "queryHaochi":
				queryHaochi(request, response);
				break;
			case "queryWeather":
				queryWeather(request, response);
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException(e);
		} catch (NamingException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}

	}

	private static void queryJingqu(ServletRequest request, ServletResponse response) throws SQLException, NamingException, IOException {
		System.out.println("1");
		JSONObject params = (JSONObject) JSONObject.parse(request.getParameter("params"));

		// 存放SQL中的参数值
		List<Object> sqlParams = new ArrayList<Object>();
		// 构造过滤条件
		List<String> filters = new ArrayList<String>();
		// 按用户ID过滤
		String ID = params.getString("id");
		if (!Util.isEmptyString(ID)) {
			filters.add("fID = ?");
			sqlParams.add(ID);
		}
		// 按用户ID过滤
		String city = params.getString("city");
		if (!Util.isEmptyString(city)) {
			filters.add("fCity = ?");
			sqlParams.add(city);
		}
		System.out.println(city);
		// TODO 自动生成的方法存根
		Connection conn = Util.getConnection(DATASOURCE_YL);
		Table table = Util.queryData(conn, "jingqus", null, filters, null, sqlParams, null, null);
		Util.writeTableToResponse(response, table);
	}

	private static void queryJingdian(ServletRequest request, ServletResponse response) throws SQLException, NamingException, IOException {
		JSONObject params = (JSONObject) JSONObject.parse(request.getParameter("params"));

		// 存放SQL中的参数值
		List<Object> sqlParams = new ArrayList<Object>();
		// 构造过滤条件
		List<String> filters = new ArrayList<String>();
		// 按用户ID过滤
		String ID = params.getString("id");
		if (!Util.isEmptyString(ID)) {
			filters.add("fID = ?");
			sqlParams.add(ID);
		}
		// 按用户belongJinggqu过滤
		String belongJingqu = params.getString("belongJingqu");
		System.out.println(belongJingqu);
		if (!Util.isEmptyString(belongJingqu)) {
			filters.add("fBelongJingqu = ?");
			sqlParams.add(belongJingqu);
		}

		// TODO 自动生成的方法存根
		Connection conn = Util.getConnection(DATASOURCE_YL);
		Table table = Util.queryData(conn, "jingdians", null, filters, null, sqlParams, null, null);
		Util.writeTableToResponse(response, table);
	}

	private static void queryHaochi(ServletRequest request, ServletResponse response) throws SQLException, NamingException, IOException {
		JSONObject params = (JSONObject) JSONObject.parse(request.getParameter("params"));

		// 存放SQL中的参数值
		List<Object> sqlParams = new ArrayList<Object>();
		// 构造过滤条件
		List<String> filters = new ArrayList<String>();
		// 按用户ID过滤
		String ID = params.getString("id");
		if (!Util.isEmptyString(ID)) {
			filters.add("fID = ?");
			sqlParams.add(ID);
		}
		// 按用户belongJinggqu过滤
		String belongJingqu = params.getString("belongScenic");
		if (!Util.isEmptyString(belongJingqu)) {
			filters.add("fBelongJingqu = ?");
			sqlParams.add(belongJingqu);
		}
		System.out.println(belongJingqu);
		// TODO 自动生成的方法存根
		Connection conn = Util.getConnection(DATASOURCE_YL);
		Table table = Util.queryData(conn, "haochis", null, filters, null, sqlParams, null, null);
		System.out.println(table.getRows().size());
		Util.writeTableToResponse(response, table);
	}

	private static void queryWeather(ServletRequest req, ServletResponse resp) throws IOException {
		JSONObject params = (JSONObject) JSONObject.parse(req.getParameter("params"));

		String param = "cityname=" + params.getString("cityName") + "&cityid=" + params.getString("cityId");
		System.out.println(param);
		String url = "http://apis.baidu.com/apistore/weatherservice/recentweathers?" + param;
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		httpGet.addHeader("apikey", "8c519eaa4acf80a9283c61fa21bdfceb");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String resultContent = new BasicResponseHandler().handleResponse(response);
		// System.out.println(resultContent);
		resp.getWriter().write(resultContent);

	}

}
