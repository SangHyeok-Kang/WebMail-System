/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.model.Pop3Agent;
import deu.cse.spring_webmail.model.UserAdminAgent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 초기 화면과 관리자 기능(사용자 추가, 삭제)에 대한 제어기
 *
 * @author skylo
 */
@Controller
@PropertySource("classpath:/system.properties")
@PropertySource("classpath:/config.properties")
@Slf4j
public class SystemController {

    @Autowired
    private ServletContext ctx;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private Environment env;

    @Value("${mysql.server.ip}")
    private String mysqlServerIp;
    @Value("${mysql.server.port}")
    private String mysqlServerPort;

    @Value("${root.id}")
    private String ROOT_ID;
    @Value("${root.password}")
    private String ROOT_PASSWORD;
    @Value("${admin.id}")
    private String ADMINISTRATOR;  //  = "admin";
    @Value("${james.control.port}")
    private Integer JAMES_CONTROL_PORT;
    @Value("${james.host}")
    private String JAMES_HOST;

    @GetMapping("/")
    public String index() {
        log.debug("index() called...");
        session.setAttribute("host", JAMES_HOST);
        session.setAttribute("debug", "false");

        return "/index";
    }

    @RequestMapping(value = "/login.do", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginDo(@RequestParam Integer menu) {
        String url = "";
        log.debug("로그인 처리: menu = {}", menu);
        switch (menu) {
            case CommandType.LOGIN:
                String host = (String) request.getSession().getAttribute("host");
                String userid = request.getParameter("userid");
                String password = request.getParameter("passwd");

                // Check the login information is valid using <<model>>Pop3Agent.
                Pop3Agent pop3Agent = new Pop3Agent(host, userid, password);
                boolean isLoginSuccess = pop3Agent.validate();

                // Now call the correct page according to its validation result.
                if (isLoginSuccess) {
                    if (isAdmin(userid)) {
                        // HttpSession 객체에 userid를 등록해 둔다.
                        session.setAttribute("userid", userid);
                        // response.sendRedirect("admin_menu.jsp");
                        url = "redirect:/admin_menu";
                    } else {
                        // HttpSession 객체에 userid와 password를 등록해 둔다.
                        session.setAttribute("userid", userid);
                        session.setAttribute("password", password);
                        // response.sendRedirect("main_menu.jsp");
                        url = "redirect:/main_menu";  // URL이 http://localhost:8080/webmail/main_menu 이와 같이 됨.
                        // url = "/main_menu";  // URL이 http://localhost:8080/webmail/login.do?menu=91 이와 같이 되어 안 좋음
                    }
                } else {
                    // RequestDispatcher view = request.getRequestDispatcher("login_fail.jsp");
                    // view.forward(request, response);
                    url = "redirect:/login_fail";
                }
                break;
            case CommandType.LOGOUT:
                session.invalidate();
                url = "redirect:/";  // redirect: 반드시 넣어야만 컨텍스트 루트로 갈 수 있음
                break;
            default:
                break;
        }
        return url;
    }

    @GetMapping("/login_fail")
    public String loginFail() {
        return "login_fail";
    }

    @GetMapping("/session_timeout")
    public String sessionTimeOut() {
        return "session_timeout";
    }

    protected boolean isAdmin(String userid) {
        boolean status = false;

        if (userid.equals(this.ADMINISTRATOR)) {
            status = true;
        }

        return status;
    }

    @GetMapping("/main_menu")
    public String mainmenu(Model model) {

        Pop3Agent pop3 = new Pop3Agent();
        pop3.setHost((String) session.getAttribute("host"));
        pop3.setUserid((String) session.getAttribute("userid"));
        pop3.setPassword((String) session.getAttribute("password"));

        String messageList = pop3.getMessageList();
        model.addAttribute("messageList", messageList);
        return "main_menu";
    }

    @GetMapping("/admin_menu")
    public String adminMenu(Model model) {

        log.debug("root.id = {}, root.password = {}, admin.id = {}",
                ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);

        model.addAttribute("userList", getUserList());
        return "admin/admin_menu";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup_user.do")
    public String signupUserDo(@RequestParam String username, @RequestParam String id, @RequestParam String password,
            RedirectAttributes attrs) {
        String urls = "";
        String userName = env.getProperty("spring.datasource.username");
        String pass = env.getProperty("spring.datasource.password");
        String jdbcDriver = env.getProperty("spring.datasource.driver-class-name");

        log.debug("signup_user.do: id = {}, password = {}, port = {}",
                id, password, JAMES_CONTROL_PORT);

        try {
            String cwd = ctx.getRealPath(".");
            UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                    ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR, mysqlServerIp, mysqlServerPort, userName, pass, jdbcDriver);
            // if (addUser successful)  사용자 등록 성공 팦업창
            // else 사용자 등록 실패 팝업창
            if (agent.addUser(id, password) && agent.addUserDB(username, id, password)) {
                attrs.addFlashAttribute("msg", String.format("사용자(%s) 추가를 성공하였습니다.", id));
            } else {
                attrs.addFlashAttribute("msg", String.format("사용자(%s) 추가를 실패하였습니다.", id));
            }
        } catch (Exception ex) {
            log.error("signup_user.do: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return "redirect:/";
    }

    @GetMapping("/add_user")
    public String addUser() {

        return "admin/add_user";
    }

    @PostMapping("/add_user.do")
    public String addUserDo(@RequestParam String username, @RequestParam String id, @RequestParam String password,
            RedirectAttributes attrs) {
        String urls = "";
        String userName = env.getProperty("spring.datasource.username");
        String pass = env.getProperty("spring.datasource.password");
        String jdbcDriver = env.getProperty("spring.datasource.driver-class-name");

        log.debug("add_user.do: id = {}, password = {}, port = {}",
                id, password, JAMES_CONTROL_PORT);

        try {
            String cwd = ctx.getRealPath(".");
            UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                    ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR, mysqlServerIp, mysqlServerPort, userName, pass, jdbcDriver);

            // if (addUser successful)  사용자 등록 성공 팦업창
            // else 사용자 등록 실패 팝업창
            if (agent.addUser(id, password) && agent.addUserDB(username, id, password)) {
                attrs.addFlashAttribute("msg", String.format("사용자(%s) 추가를 성공하였습니다.", id));
            } else {
                attrs.addFlashAttribute("msg", String.format("사용자(%s) 추가를 실패하였습니다.", id));
            }
        } catch (Exception ex) {
            log.error("add_user.do: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return "redirect:/admin_menu";
    }

    @GetMapping("/delete_user")
    public String deleteUser(Model model) {
        log.debug("delete_user called");
        model.addAttribute("userList", getUserList());
        return "admin/delete_user";
    }

    /**
     *
     * @param selectedUsers <input type=checkbox> 필드의 선택된 이메일 ID. 자료형: String[]
     * @param attrs
     * @return
     */
    @PostMapping("delete_user.do")
    public String deleteUserDo(@RequestParam String[] selectedUsers, RedirectAttributes attrs) {
        String urls = "";
        String userName = env.getProperty("spring.datasource.username");
        String pass = env.getProperty("spring.datasource.password");
        String jdbcDriver = env.getProperty("spring.datasource.driver-class-name");

        log.debug("delete_user.do: selectedUser = {}", List.of(selectedUsers));

        try {
            String cwd = ctx.getRealPath(".");
            UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                    ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR, mysqlServerIp, mysqlServerPort, userName, pass, jdbcDriver);
            agent.deleteUsers(selectedUsers);  // 수정!!!
        } catch (Exception ex) {
            log.error("delete_user.do : 예외 = {}", ex);
        }

        return "redirect:/admin_menu";
    }

    private List<String> getUserList() {
        String urls = "";
        String userName = env.getProperty("spring.datasource.username");
        String pass = env.getProperty("spring.datasource.password");
        String jdbcDriver = env.getProperty("spring.datasource.driver-class-name");

        String cwd = ctx.getRealPath(".");
        UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR, mysqlServerIp, mysqlServerPort, userName, pass, jdbcDriver);
        List<String> userList = agent.getUserList();
        log.debug("userList = {}", userList);

        //(주의) root.id와 같이 '.'을 넣으면 안 됨.
        userList.sort((e1, e2) -> e1.compareTo(e2));
        return userList;
    }

    @GetMapping("/img_test")
    public String imgTest() {
        return "img_test/img_test";
    }

    /**
     * https://34codefactory.wordpress.com/2019/06/16/how-to-display-image-in-jsp-using-spring-code-factory/
     *
     * @param imageName
     * @return
     */
    @RequestMapping(value = "/get_image/{imageName}")
    @ResponseBody
    public byte[] getImage(@PathVariable String imageName) {
        try {
            String folderPath = ctx.getRealPath("/WEB-INF/views/img_test/img");
            return getImageBytes(folderPath, imageName);
        } catch (Exception e) {
            log.error("/get_image 예외: {}", e.getMessage());
        }
        return new byte[0];
    }

    private byte[] getImageBytes(String folderPath, String imageName) {
        ByteArrayOutputStream byteArrayOutputStream;
        BufferedImage bufferedImage;
        byte[] imageInByte;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bufferedImage = ImageIO.read(new File(folderPath + File.separator + imageName));
            String format = imageName.substring(imageName.lastIndexOf(".") + 1);
            ImageIO.write(bufferedImage, format, byteArrayOutputStream);
            byteArrayOutputStream.flush();
            imageInByte = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return imageInByte;
        } catch (FileNotFoundException e) {
            log.error("getImageBytes 예외: {}", e.getMessage());
        } catch (Exception e) {
            log.error("getImageBytes 예외: {}", e.getMessage());
        }
        return null;
    }

    @GetMapping("read_book")
    public String bookRead() {
        return "read_book";
    }

    @GetMapping("add_book")
    public String bookAdd() {
        return "add_book";
    }

    @GetMapping("search_book")
    public String bookMod() {
        return "search_book";
    }

    @GetMapping("mod_book")
    public String bookMod2() {
        return "mod_book";
    }

    @GetMapping("del_book")
    public String bookDel() {
        return "del_book";
    }
    
    @PostMapping("/insert")
    public String insertAddress(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("adder") String adder, RedirectAttributes attrs) {
        try {
            final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
            final String JdbcUrl = "jdbc:mysql://113.198.236.222:9090/mail?serverTimezone=Asia/Seoul";
            final String User = "root";
            final String Password = "1q2w3e4r";

            Class.forName(JdbcDriver);

            try (Connection conn = DriverManager.getConnection(JdbcUrl, User, Password); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO addrbook (name, email, phone, adder) VALUES (?, ?, ?, ?)")) {

                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, phone);
                pstmt.setString(4, adder);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    attrs.addFlashAttribute("msg", String.format("주소록 추가되었습니다."));
                    return "redirect:read_book";
                } else {
                    attrs.addFlashAttribute("msg", String.format("주소록 추가 실패하였습니다."));
                    return "redirect:add_book";
                }
            }
        } catch (Exception ex) {
            // 예외 처리 로직
            ex.printStackTrace();
        }
        return "redirect:read_book";
    }
    
    @PostMapping("/update")
    public String updateAddress(
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone, RedirectAttributes attrs) {
        try {
            final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
            final String JdbcUrl = "jdbc:mysql://113.198.236.222:9090/mail?serverTimezone=Asia/Seoul";
            final String User = "root";
            final String Password = "1q2w3e4r";

            Class.forName(JdbcDriver);

            try (Connection conn = DriverManager.getConnection(JdbcUrl, User, Password); PreparedStatement pstmt = conn.prepareStatement("UPDATE addrbook SET name = ?, phone = ? WHERE email = ?")) {

                pstmt.setString(1, name);
                pstmt.setString(2, phone);
                pstmt.setString(3, email);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    // 수정 성공 시 처리할 로직
                    attrs.addFlashAttribute("msg", String.format("주소록 수정이 완료되었습니다."));
                    return "redirect:read_book";
                } else {
                    // 수정 실패 시 처리할 로직
                    attrs.addFlashAttribute("msg", String.format("주소록 수정 실패하였습니다."));
                    return "redirect:search_book";
                }
            }
        } catch (Exception ex) {
            // 예외 처리 로직
            ex.printStackTrace();
            // 에러 메시지 등을 처리
        }

        // 리디렉션할 페이지로 이동
        return "redirect:read_book";
    }
    
    @PostMapping("/delete")
    public String deleteAddress(@RequestParam("email") String email, Model model, RedirectAttributes attrs) {
        try {
            final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
            final String JdbcUrl = "jdbc:mysql://113.198.236.222:9090/mail?serverTimezone=Asia/Seoul";
            final String User = "root";
            final String Password = "1q2w3e4r";

            Class.forName(JdbcDriver);

            try (Connection conn = DriverManager.getConnection(JdbcUrl, User, Password); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM addrbook WHERE email = ?")) {
                pstmt.setString(1, email);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    // 삭제 성공 시 처리할 로직
                    attrs.addFlashAttribute("msg", String.format("주소록 삭제되었습니다."));
                    return "redirect:del_book";
                } else {
                    // 삭제 실패 시 처리할 로직
                    attrs.addFlashAttribute("msg", String.format("주소록 삭제 실패하였습니다."));
                }
            }
        } catch (Exception ex) {
            // 예외 처리 로직
            ex.printStackTrace();
        }

        return "del_book";
    }
    
    @PostMapping("/search")
    public String searchBook(@RequestParam("email") String email, Model model) {
        try {
            final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
            final String JdbcUrl = "jdbc:mysql://113.198.236.222:9090/mail?serverTimezone=Asia/Seoul";
            final String User = "root";
            final String Password = "1q2w3e4r";

            Class.forName(JdbcDriver);

            try (Connection conn = DriverManager.getConnection(JdbcUrl, User, Password); PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM addrbook WHERE email = ?")) {

                pstmt.setString(1, email);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String name = rs.getString("name");
                    String phone = rs.getString("phone");

                    model.addAttribute("email", email);
                    model.addAttribute("name", name);
                    model.addAttribute("phone", phone);

                    return "mod_book";
                } else {
                    
                }
            }
        } catch (Exception ex) {
            // 예외 처리 로직
            ex.printStackTrace();
        }

        return "redirect:search_book";
    }
}
