package com.mucommander.commons.file.protocol.fooex;

import com.mucommander.commons.file.AbstractFile;
import com.mucommander.commons.file.FileURL;
import com.mucommander.commons.file.protocol.ProtocolProvider;

import java.io.IOException;

public class FOOEXProtocolProvider implements ProtocolProvider {


    @Override
    public AbstractFile getFile(FileURL url, Object... instantiationParams) throws IOException {
        return new FOOEXFile(url);
    }
}
