package com.ape;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.IOUtils;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.XmlSolverFactory;
import play.api.Play;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Gaetan on 21/11/2015.
 */
public class ApeXmlSolverFactory implements SolverFactory {

    public ApeXmlSolverFactory configure(String resource) {
        InputStream in = Play.current().classloader().getResourceAsStream(resource);
        if(in == null) {
            throw new IllegalArgumentException("The solver configuration (" + resource + ") does not exist.");
        } else {
            return this.configure(in);
        }
    }


    private XStream xStream;
    private SolverConfig solverConfig;

    public static XStream buildXstream() {
        XStream xStream = new XStream();
        xStream.setMode(1002);
        xStream.processAnnotations(SolverConfig.class);
        return xStream;
    }

    public ApeXmlSolverFactory() {
        this.solverConfig = null;
        this.xStream = buildXstream();
    }

    public ApeXmlSolverFactory(String resource) {
        this();
        this.configure(resource);
    }

    public void addXstreamAnnotations(Class... xstreamAnnotations) {
        this.xStream.processAnnotations(xstreamAnnotations);
    }



    public ApeXmlSolverFactory configure(InputStream in) {
        InputStreamReader reader = null;

        ApeXmlSolverFactory e;
        try {
            reader = new InputStreamReader(in, "UTF-8");
            e = this.configure((Reader)reader);
        } catch (UnsupportedEncodingException var7) {
            throw new IllegalStateException("This vm does not support UTF-8 encoding.", var7);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(in);
        }

        return e;
    }

    public ApeXmlSolverFactory configure(Reader reader) {
        this.solverConfig = (SolverConfig)this.xStream.fromXML(reader);
        return this;
    }

    public SolverConfig getSolverConfig() {
        if(this.solverConfig == null) {
            throw new IllegalStateException("The solverConfig (" + this.solverConfig + ") is null," + " call configure(...) first.");
        } else {
            return this.solverConfig;
        }
    }

    public Solver buildSolver() {
        if(this.solverConfig == null) {
            throw new IllegalStateException("The solverConfig (" + this.solverConfig + ") is null," + " call configure(...) first.");
        } else {
            return this.solverConfig.buildSolver();
        }
    }
}
