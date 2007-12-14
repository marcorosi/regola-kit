#!/usr/bin/ruby

require 'find'
require 'fileutils'

main_dir = '../regola-example-simple/'
archetype_dir = 'src/main/resources/archetype-resources/'
excludes = ['.svn','classes', 'target', 'lib', 'hibernate-tools', 'tmp']
excludes_files = ['/org/kion/plitvice', '/\.', 'src/main/java', '/java/org/regola/test','main/resources/regola-config/applicationContext-']

FileUtils.remove_dir(archetype_dir + 'src') if FileTest.exist?(archetype_dir + 'src') 

target = { 'src/main/java'      => '<resources>', # Per evitare il filtraggio dei sorgenti, dovrebbe essere  'src/main/java' => '<sources>'
           'src/main/resources' => '<resources>',
           'src/main/webapp'    => '<resources>',
           'src/test/java'      => '<testResources>', # Per evitare il filtraggio dei sorgenti, dovrebbe essere  'src/test/java' => '<testSources>',
           'src/test/resources' => '<testResources>',
           'src/site'           => '<siteResources>'}

tag    = { '<sources>'       => 'source',
           '<resources>'     => 'resource',
           '<testSources>'   => 'source',
           '<testResources>' => 'resource',
           '<siteResources>' => 'resource' }

archetype = {}
pos = 0
dirs = [main_dir]

for dir in dirs
  Find.find(dir) do |path|
    if FileTest.directory?(path)
      if excludes.include?(File.basename(path))
        Find.prune       # Don't look any further into this directory.
      else
        next
      end
    else
       out = false       
       excludes_files.each { |exl| if path.match(exl) : out = true end  }     
 
       if (!out ) then
        pos += 1
        relative_path = path.sub(main_dir, '')
        entry = '   <source>' + relative_path + '</source>' 
        #puts entry
        
        added = false
        target.each_key do |key|  
           if  (path.match(key)) : 
              archetype[target[key]] ||= [] 
              archetype[target[key]]<< "   <#{tag[target[key]]} filtered=\"false\">#{relative_path}</#{tag[target[key]]}>"
              added = true
              puts relative_path
              FileUtils.mkdir_p(File.dirname(archetype_dir + relative_path))
              FileUtils.cp(path, archetype_dir + relative_path) 
              #puts 'cp ' + path + ' ' + archetype_dir + relative_path
           end
        end
        # puts entry unless added

       end
    end
  end
end

FileUtils.mkdir('src/main/resources/META-INF/') unless FileTest.exist?('src/main/resources/META-INF/') 

outfile = File.new('src/main/resources/META-INF/archetype.xml', "w")  

archetype['<resources>'] << '   <resource>parent-pom.xml</resource>'
archetype['<resources>'] << '   <resource>wsdl</resource>'
#archetype['<resources>'] << '   <resource>src/main/java</resource>'

#puts "Totale file: #{pos}"
#ora scriviamo il file

outfile.puts <<END
<archetype>
  <id>webapp-archetype</id>
END

archetype.each_key do |section|
   outfile.puts ' ' + section
   archetype[section].each { |entry| outfile.puts entry }
   outfile.puts ' ' + section.sub('<', '</')
end

outfile.puts '</archetype>'
