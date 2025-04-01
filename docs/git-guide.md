# Git 使用指南

## 一、基本配置

### 1.1 初始配置
```bash
# 设置用户名和邮箱
git config --global user.name "你的名字"
git config --global user.email "你的邮箱"

# 查看配置
git config --list
```

## 二、常用命令

### 2.1 创建/克隆仓库
```bash
# 在当前目录初始化仓库
git init

# 克隆远程仓库
git clone <仓库地址>
```

### 2.2 日常操作
```bash
# 查看状态
git status

# 添加文件到暂存区
git add <文件名>     # 添加指定文件
git add .           # 添加所有文件

# 提交更改
git commit -m "提交说明"

# 查看提交历史
git log
```

### 2.3 分支操作
```bash
# 查看分支
git branch          # 查看本地分支
git branch -r       # 查看远程分支
git branch -a       # 查看所有分支

# 创建分支
git branch <分支名>

# 切换分支
git checkout <分支名>
# 或者使用新命令
git switch <分支名>

# 创建并切换分支
git checkout -b <分支名>
# 或者使用新命令
git switch -c <分支名>
```

### 2.4 远程操作
```bash
# 推送到远程
git push origin <分支名>

# 拉取远程更新
git pull origin <分支名>

# 查看远程仓库
git remote -v
```

## 三、开发流程示例

### 3.1 新功能开发
```bash
# 1. 确保主分支是最新的
git checkout main
git pull origin main

# 2. 创建功能分支
git checkout -b feature/new-feature

# 3. 开发功能并提交
git add .
git commit -m "feat: 添加新功能"

# 4. 推送到远程
git push origin feature/new-feature
```

### 3.2 Bug修复
```bash
# 1. 从主分支创建修复分支
git checkout -b fix/bug-fix

# 2. 修复bug并提交
git add .
git commit -m "fix: 修复xxx问题"

# 3. 推送到远程
git push origin fix/bug-fix
```

## 四、提交规范

### 4.1 提交类型
- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式调整
- refactor: 重构
- test: 测试相关
- chore: 构建/工具相关

### 4.2 提交格式
```
<类型>: <简短描述>

[可选的详细描述]

[可选的关闭issue引用]
```

示例：
```bash
git commit -m "feat: 添加用户登录功能

实现了用户登录和JWT token生成功能

Closes #123"
```

## 五、常见问题处理

### 5.1 撤销修改
```bash
# 撤销工作区修改
git checkout -- <文件名>
# 或使用新命令
git restore <文件名>

# 撤销暂存区修改
git reset HEAD <文件名>
# 或使用新命令
git restore --staged <文件名>

# 撤销提交
git reset --soft HEAD^    # 撤销提交但保留修改
git reset --hard HEAD^    # 撤销提交并丢弃修改
```

### 5.2 解决冲突
```bash
# 1. 拉取最新代码
git pull origin <分支名>

# 2. 解决冲突
# 手动编辑冲突文件，选择要保留的内容

# 3. 添加并提交解决的文件
git add .
git commit -m "fix: 解决冲突"
```

## 六、最佳实践

1. **经常提交**
   - 小步提交，保持提交粒度适中
   - 每个提交只做一件事

2. **分支管理**
   - 主分支保持稳定
   - 功能开发使用特性分支
   - 及时删除已合并的分支

3. **提交信息**
   - 使用规范的提交格式
   - 清晰描述改动内容
   - 必要时添加详细说明

4. **代码同步**
   - 经常拉取远程更新
   - 解决冲突及时处理
   - 推送前先拉取 