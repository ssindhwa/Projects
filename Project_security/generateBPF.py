
TEMPLATE_UPROBE = """
int uprobe_{func_name}(struct pt_regs *ctx) {{
    int my_id = {my_id}; 
    u64 key = 1;
    u64 * val = current_libc.lookup(&key);

    if(val){{
        if(*val == -1 || *val != my_id) bpf_send_signal(9);
    }}
    return 0;
}}
"""

TEMPLATE_URETPROBE = """
int uretprobe_{func_name}(struct pt_regs *ctx) {{

    u64 key = 1;
    u64 val = -1;
    current_libc.update(&key, &val);
    return 0;   
}}
"""

# Function to read specific functions from op.txt and generate the code
def generate_code(libc_file="libc.txt", op_file="op.txt", output_file="logic1.c"):
    # Read the function names from libc.txt
    with open(libc_file, "r") as f:
        all_functions = [line.strip() for line in f.readlines()]

    # Read the specific functions from op.txt
    with open(op_file, "r") as f:
        specific_functions = [line.strip() for line in f.readlines()]

    with open(output_file, "w") as f:
        for func_name in specific_functions:
            if func_name in all_functions:
                # Find the index (my_id) of the function in libc.txt
                my_id = all_functions.index(func_name) + 1  # +1 because IDs start from 1
                
                # Generate uprobe and uretprobe for each function using the my_id
                uprobe_code = TEMPLATE_UPROBE.format(func_name=func_name, my_id=my_id)
                uretprobe_code = TEMPLATE_URETPROBE.format(func_name=func_name)
                
                # Write the generated code to the file
                f.write(uprobe_code)
                f.write("\n")
                f.write(uretprobe_code)
                f.write("\n")
    
    print(f"Generated code has been written to {output_file}")

# Call the function to generate the code
generate_code()
