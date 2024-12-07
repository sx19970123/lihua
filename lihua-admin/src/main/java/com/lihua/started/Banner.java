package com.lihua.started;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 系统启动成功打印 banner
 */
@Component
public class Banner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        System.out.println("""

                     __   _____ _______   _      _____  _______    _____ _    _  _____ _____ ______  _____ _____       __
                    / /  / ____|__   __| / \\    |  __ \\|__   __|  / ____| |  | |/ ____/ ____|  ____|/ ____/ ____|     / /
                   / /  | (___    | |   /   \\   | |__) |  | |    | (___ | |  | | |   | |    | |__  | (___| (___      / /\s
                  / /    \\___ \\   | |  / /_\\ \\  |  _  /   | |     \\___ \\| |  | | |   | |    |  __|  \\___ \\\\___ \\    / / \s
                 / /     ____) |  | | / _____ \\ | | \\ \\   | |     ____) | |__| | |___| |____| |____ ____) |___) |  / /\s
                /_/     |_____/   |_|/_/     \\_\\|_|  \\_\\  |_|    |_____/ \\____/ \\_____\\_____|______|_____/_____/  /_/\s
                """
        );
    }
}
