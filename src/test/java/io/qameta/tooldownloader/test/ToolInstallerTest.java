package io.qameta.tooldownloader.test;

import io.qameta.tooldownloader.InstallationConfiguration;
import io.qameta.tooldownloader.ToolConfiguration;
import io.qameta.tooldownloader.ToolInstallation;
import io.qameta.tooldownloader.archivers.ArchiveType;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public class ToolInstallerTest {

    @Test
    public void downloadTool() {
        Optional<ToolInstallation> installation = new InstallationConfiguration.Builder(Paths.get("/tmp/test"))
                .withArchiveType(ArchiveType.ZIP)
                .withDownloadUrl("https://dl.bintray.com/qameta/generic/io/qameta/allure/allure/2.0.1/allure-2.0.1.zip")
                .withsubdirectoryInArchive("allure-2.0.1")
                .withExpectedSha256("e66a396f157bd4cc42b0fe41e48e28eb1561692824c1953045272c7a7b8e6d40")
                .build().download();
        ToolConfiguration configuration = new ToolConfiguration.Builder()
                .withName("allure")
                .withPathToExecutable("bin/allure")
                .withValidationCommand("--version")
                .withValidationCondition(result -> result.isSuccess() && result.getOutput().contains("2.0.1")).build();
        assertThat(installation.map(i -> i.getTool(configuration))).isNotNull();
    }
}
