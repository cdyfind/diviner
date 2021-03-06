package com.diviner.netty;

import ch.qos.logback.core.db.dialect.MySQLDialect;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.diviner.netty.pojo.User;
import com.diviner.netty.serialize.impl.JSONSerializer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.AsciiString;
import io.netty.util.Attribute;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpHelloworldServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private  static final Logger logger = LoggerFactory.getLogger(HttpHelloworldServerHandler.class);

    private HttpHeaders httpHeaders;
    private HttpRequest httpRequest;
    private FullHttpRequest fullHttpRequest;


    private static final String FAVICON_ICO = "/favicon.ico";
    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-length");
    private static final AsciiString CONNECTION = AsciiString.cached("connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        User user = new User();
        user.setUserName("fordiddenDiviner");
        user.setDate(new Date());

        if (msg instanceof HttpRequest){
            httpRequest = (HttpRequest) msg;
            httpHeaders = httpRequest.headers();
            String uri = httpRequest.uri();
            if (uri.equals(FAVICON_ICO)){
                return;
            }
            HttpMethod httpMethod = httpRequest.method();
            if (httpMethod.equals(HttpMethod.GET)){
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
                Map<String, List<String>> uriAttributes = queryStringDecoder.parameters();
                //???????????????????????????
                for (Map.Entry<String,List<String>> attr : uriAttributes.entrySet()){
                    for (String attrVal : attr.getValue()){
                        logger.info(attr.getKey()+"="+attrVal);
                    }
                }
                user.setMethod("get");

            }else if (httpMethod.equals(HttpMethod.POST)){
                fullHttpRequest =  (FullHttpRequest)msg;
                //???????????????Contact_type??????body??????
                dealWithContentType();
                user.setMethod("post");
            }
            JSONSerializer jsonSerializer = new JSONSerializer();
            byte[] content = jsonSerializer.serializer(user);

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());

            boolean keepAlive = HttpUtil.isKeepAlive(httpRequest);
            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, KEEP_ALIVE);
                ctx.write(response);
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * ????????????????????????Content-Type ??? post??????
     */
    private void dealWithContentType() throws Exception{
        String contentType = getContentType();
        //????????????HttpJsonDecoder
        if (contentType.equals("application/json")){
            String jsonStr = fullHttpRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            if (!StringUtil.isNullOrEmpty(jsonStr)){
                JSONObject obj = JSON.parseObject(jsonStr);
                for (Map.Entry<String,Object> item:obj.entrySet()){
                    logger.info(item.getKey()+"="+item.getValue().toString());
                }
            }

        }else if (contentType.equals("application/x-www-form-urlencoded")){
            //?????????????????? QueryStringDecoder
//            String jsonStr = fullHttpRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
//            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(jsonStr,false);
//            Map<String,List<String>> uriAttrbutes = queryStringDecoder.parameters();
//            for (Map.Entry<String,List<String>> attr:uriAttrbutes.entrySet()){
//                for (String attrVal:attr.getValue()){
//                    logger.info(attr.getKey()+"="+attrVal);
//                }
//            }
            //??????????????????HttpPostRequestDecoder
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullHttpRequest);
            decoder.offer(fullHttpRequest);
            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
            for (InterfaceHttpData parm:parmList) {
                if (parm instanceof MixedAttribute) {
                    MixedAttribute data = (MixedAttribute) parm;
                    logger.info(data.getName() + ":" + data.getValue());
                }
            }
        }else if (contentType.equals("multipart/form-data")){
            //TODO ??????????????????
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullHttpRequest);
            decoder.offer(fullHttpRequest);
            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
            for (InterfaceHttpData parm:parmList){
                if (parm instanceof MixedAttribute){
                    MixedAttribute data = (MixedAttribute) parm;
                    logger.info(data.getName()+":"+data.getValue());
                }
                if (parm instanceof MixedFileUpload){
                    MixedFileUpload fileUpload = (MixedFileUpload)parm;
                    logger.info("??????????????????"+fileUpload);
                    File file = fileUpload.getFile();
                    //???????????????
                    InputStream in = new FileInputStream(file);
                    savePic(in,fileUpload.getFilename());
                }
            }

        }else {
            //do nothing
        }
    }

    private String getContentType(){
        String typeStr = httpHeaders.get("Content-Type").toString();
        String[] list = typeStr.split(";");
        return list[0];
    }

    private void savePic(InputStream inputStream, String fileName) {

        OutputStream os = null;
        try {
            String path = "D:\\testFile\\";
            // 2????????????????????????
            // 1K???????????????
            byte[] bs = new byte[1024];
            // ????????????????????????
            int len;
            // ???????????????????????????????????????

            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            // ????????????
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // ???????????????????????????
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
