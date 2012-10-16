def run(className, args = [])
  result = `#{File.dirname(__FILE__) + "/run.rb " + className + " " + args.join(" ")}`
  return "#{className},#{args.join(",")},#{result}"
end