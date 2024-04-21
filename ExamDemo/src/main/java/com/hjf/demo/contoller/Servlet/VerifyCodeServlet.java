package com.hjf.demo.contoller.Servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.hjf.demo.utils.Email_Utils;
import com.hjf.demo.utils.JSON_Utils;
import com.hjf.demo.utils.SetResponse_Utils;
import com.hjf.demo.utils.VerifyCode_Utils;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/verifyCode")
public class VerifyCodeServlet extends BaseServlet{
    public void sendEmailCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String email = rootNode.get("email").asText();
        String code = VerifyCode_Utils.getNumCode(6);
        request.getSession().removeAttribute("PictureCode");
        request.getSession().setAttribute("PictureCode", code);
        String content = "<body>验证码为: " + code + " </body>";
        try {
            Email_Utils.sendEmail(email, "ExamDemo的验证码", content);
            SetResponse_Utils.setResponse(response ,200, "success", "验证码已发送");
        }catch (AddressException e){
            SetResponse_Utils.setResponse(response ,500, "false", "邮箱不存在");
        }catch (MessagingException e){
            SetResponse_Utils.setResponse(response ,500, "false", "发送失败");
        }
    }
    public void getPictureCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int width = 200;
        int height = 69;
        BufferedImage verifyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        String randomText = VerifyCode_Utils.drawRandomText(width, height, verifyImg);
        request.getSession().removeAttribute("PictureCode");
        request.getSession().setAttribute("PictureCode", randomText);
        response.setContentType("image/png");//必须设置响应内容类型为图片，否则前台不识别
        OutputStream os = response.getOutputStream(); //获取文件输出流
        ImageIO.write(verifyImg, "png", os);//输出图片流
        os.flush();
        os.close();
        SetResponse_Utils.setResponse(response ,200, "success", "验证码已发送");
    }

    public void VerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String codeType = rootNode.get("codeType").asText();
        String userCode = rootNode.get("userCode").asText();
        String verifyCode = (String) request.getSession().getAttribute(codeType);
        if (verifyCode.equals(userCode)){
            request.getSession().removeAttribute(codeType);
            SetResponse_Utils.setResponse(response ,200, "success", "验证通过");
        }else{
            SetResponse_Utils.setResponse(response ,400, "false", "验证码错误");
        }
    }
}
