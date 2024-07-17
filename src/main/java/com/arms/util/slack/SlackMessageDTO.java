package com.arms.util.slack;


import com.slack.api.model.Attachment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SlackMessageDTO {

    private String title;
    private String footer;
    private String footerIcon;


    @Builder
    private SlackMessageDTO(String title, String footer, String footerIcon) {
        this.title = title;
        this.footer = footer;
        this.footerIcon = footerIcon;
    }


    public Attachment parseAttachment(String text) {
        return Attachment.builder().title(this.title).text(text).fallback(text).footer(this.footer)
                .footerIcon(this.footerIcon).build();
    }
}

