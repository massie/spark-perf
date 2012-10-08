#!/usr/bin/env ruby

Dir.glob(File.dirname(__FILE__) + "/*").reject do |f| 
  f.end_with?("all.rb") || f.end_with?("run.rb")
end.sort.each do|f|
   require File.dirname(__FILE__) + "/#{f}"
end