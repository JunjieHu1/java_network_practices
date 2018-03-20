import java.lang.annotation.Documented;

@Documented
@interface ClassPreamble {

    String author() default "junjiehu";
    String date() default "2018-03-20";
    int currentRevision() default 1;
    String lastModified() default "N/A";
    String lastModifiedBy() default "N/A";
    String[] reviewer();
}