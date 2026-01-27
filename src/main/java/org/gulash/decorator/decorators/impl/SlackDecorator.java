package org.gulash.decorator.decorators.impl;

import org.gulash.decorator.Notifier;
import org.gulash.decorator.decorators.BaseNotifierDecorator;

/**
 * Еще один конкретный декоратор, добавляющий уведомления в Slack.
 */
public class SlackDecorator extends BaseNotifierDecorator {
    private final String slackUserId;

    public SlackDecorator(Notifier wrapper, String slackUserId) {
        super(wrapper);
        this.slackUserId = slackUserId;
    }

    @Override
    public void send(String message) {
        super.send(message);
        sendToSlack(message);
    }

    private void sendToSlack(String message) {
        System.out.println("[Slack] Отправка уведомления пользователю " + slackUserId + ": " + message);
    }
}
