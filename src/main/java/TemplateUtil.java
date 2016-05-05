import freemarker.cache.ClassTemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class TemplateUtil {

    private static final Configuration cfg = new Configuration(Configuration.VERSION_2_3_24);

    static {
        cfg.setTemplateLoader(new ClassTemplateLoader(TemplateUtil.class, ""));
        cfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
    }

    public static void render(String name, Map<String, Object> data, Writer out) throws IOException, ServletException {
        Template template = cfg.getTemplate(name);
        try {
            template.process(data, out);
        } catch (TemplateException e) {
            throw new ServletException(e);
        }
    }
}
