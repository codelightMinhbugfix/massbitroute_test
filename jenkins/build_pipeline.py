#!/usr/bin/python

from jinja2 import Environment, FileSystemLoader
import os, re

#CONFIG_PATH = '/var/jenkins_home/jobs'
#CONFIG_PATH = '/var/lib/docker/volumes/jenkins_home/_data/jobs'
components = [
    {
        'component_name': 'api',
        'repo_name': 'massbitroute',
        'DEFAULT_COMPONENT_TAG_IN_BASE': 'export api=$(cat .vars/api)',
        'PR_COMPONENT_TAG_IN_BASE': 'export api=${PR_NUMBER}',
        'jenkins_name': 'MassbitAPI'
    },
    {
        'component_name': 'gwman',
        'repo_name': 'massbitroute_gwman',
        'DEFAULT_COMPONENT_TAG_IN_BASE': 'export gwman=$(cat .vars/gwman)',
        'PR_COMPONENT_TAG_IN_BASE': 'export gwman=${PR_NUMBER}',
        'jenkins_name': 'MassbitGwMan'
    },
    {
        'component_name': 'session',
        'repo_name': 'massbitroute_session',
        'DEFAULT_COMPONENT_TAG_IN_BASE': 'export session=$(cat .vars/session)',
        'PR_COMPONENT_TAG_IN_BASE': 'export session=${PR_NUMBER}',
        'jenkins_name': 'MassbitSession'
    },
    {
        'component_name': 'git',
        'repo_name': 'massbitroute_git',
        'DEFAULT_COMPONENT_TAG_IN_BASE': 'export api=$(cat .vars/git)',
        'PR_COMPONENT_TAG_IN_BASE': 'export git=${PR_NUMBER}',
        'jenkins_name': 'MassbitGit'
    },
    {
        'component_name': 'monitor',
        'repo_name': 'massbitroute_monitor',
        'DEFAULT_COMPONENT_TAG_IN_BASE': 'export monitor=$(cat .vars/monitor)',
        'PR_COMPONENT_TAG_IN_BASE': 'export monitor=${PR_NUMBER}',
        'jenkins_name': 'MassbitMonitor'
    },
    {
        'component_name': 'stat',
        'repo_name': 'massbitroute_stat',
        'DEFAULT_COMPONENT_TAG_IN_BASE': 'export api=$(cat .vars/stat)',
        'PR_COMPONENT_TAG_IN_BASE': 'export stat=${PR_NUMBER}',
        'jenkins_name': 'MassbitStat'
    },
    {
        'component_name': 'node',
        'repo_name': 'massbitroute_node',
        'DEFAULT_COMPONENT_TAG_IN_BASE': 'export api=$(cat .vars/node)',
        'PR_COMPONENT_TAG_IN_BASE': 'export node=${PR_NUMBER}',
        'jenkins_name': 'MassbitNode'
    },
    {
        'component_name': 'gateway',
        'repo_name': 'massbitroute_gateway',
        'DEFAULT_COMPONENT_TAG_IN_BASE': 'export api=$(cat .vars/gateway)',
        'PR_COMPONENT_TAG_IN_BASE': 'export gateway=${PR_NUMBER}',
        'jenkins_name': 'MassbitGateway'
    },
    {
        'component_name': 'fisherman',
        'repo_name': 'massbitroute_fisherman',
        'DEFAULT_COMPONENT_TAG_IN_BASE': 'export fisherman=$(cat .vars/fisherman)',
        'PR_COMPONENT_TAG_IN_BASE': 'export fisherman=${PR_NUMBER}',
        'jenkins_name': 'Fisherman'
    },
]
vars = {
    'TEST_BRANCH': 'feature/jenkins-template'
}
setup_stages = [
    {'name': 'Create docker network',       'steps': ['bash ./create_docker_network.sh'] },
    {'name': 'Get latest git tag',          'steps': ['bash ./check_latest_tag.sh _read_latest_git_tags'] },
    {'name': 'Prepare runtime environment', 'steps': ['bash -x prepare_runtime.sh'] },
    {'name': 'Prepare proxy',               'steps': ['bash -x prepare_proxy.sh'] },
    {'name': 'Create git docker',           'steps': ['bash -x create_git.sh'] },
    {'name': 'Start docker compose',        'steps': ['bash -x create_docker_compose.sh'] },
    {'name': 'Start stat docker compose',   'steps': ['bash -x create_stat_docker_compose.sh'] },
    #{'name': 'Run API test',                'steps': ['bash -x run_component_api_test.sh'] },
]

scenarios = os.listdir('../end2end/scenarios-enable/')
scenarios.remove("000_init.sh")
file_loader = FileSystemLoader('templates')
env = Environment(loader=file_loader)

config_template = env.get_template('./jenkins.config.template')
pipeline_template = env.get_template('./jenkins.pipeline.template')
CONFIG_DIR = os.getenv('CONFIG_DIR')
for component in components:
    print(component)
    pipeline = pipeline_template.render(comps=component, stages=setup_stages, scenarios=scenarios, vars=vars )
    f = open(component["component_name"] + '.pipeline', "w")
    f.write(pipeline)
    f.close()
    if os.path.isdir(CONFIG_DIR):
        pipeline = re.sub(r'&','&amp;',pipeline)
        pipeline = re.sub(r'\'','&apos;',pipeline)
        pipeline = re.sub(r'"','&quot;',pipeline)
        pipeline = re.sub(r'>','&gt;',pipeline)
        config = config_template.render(PIPELINE=pipeline)
        config_path = os.path.join(CONFIG_DIR, component["jenkins_name"])
        # Create config directory
        if not os.path.isdir(config_path):
            os.makedirs(config_path)
        f = open(config_path + '/config.xml', "w")
        f.write(config)
        f.close()
