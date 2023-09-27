package foo.bar;

import java.nio.file.Paths;
import java.util.logging.Level;
import schemacrawler.schemacrawler.LimitOptionsBuilder;
import schemacrawler.schemacrawler.LoadOptionsBuilder;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.executable.SchemaCrawlerExecutable;
import schemacrawler.tools.options.OutputOptionsBuilder;
import us.fatehi.utility.LoggingConfig;
import us.fatehi.utility.datasource.DatabaseConnectionSource;
import us.fatehi.utility.datasource.DatabaseConnectionSources;
import us.fatehi.utility.datasource.MultiUseUserCredentials;

public class Main {

  public static void main(final String[] args) {

    // Set log level
    new LoggingConfig(Level.OFF);

    // Create the options
    final LimitOptionsBuilder limitOptionsBuilder = LimitOptionsBuilder.builder()
        .includeTables(tableFullName -> tableFullName.startsWith("foo.bar"));
    final LoadOptionsBuilder loadOptionsBuilder =
        LoadOptionsBuilder.builder().withSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
    final OutputOptionsBuilder outputOptions = OutputOptionsBuilder.builder()
        .withOutputFormatValue("json").withOutputFile(Paths.get("output.json"));
    final SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
        .withLimitOptions(limitOptionsBuilder.toOptions())
        .withLoadOptions(loadOptionsBuilder.toOptions());

    // Get the schema definition
    final DatabaseConnectionSource dataSource = getDataSource();

    final SchemaCrawlerExecutable command = new SchemaCrawlerExecutable("serialize");
    command.setSchemaCrawlerOptions(options);
    command.setOutputOptions(outputOptions.toOptions());
    command.setDataSource(dataSource);
    command.execute();
  }

  private static DatabaseConnectionSource getDataSource() {
    final String connectionUrl = "jdbc:sqlite::resource:test.db";
    return DatabaseConnectionSources.newDatabaseConnectionSource(connectionUrl,
        new MultiUseUserCredentials("", ""));
  }
}
